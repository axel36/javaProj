import Basics exposing (toString)
import Date exposing (Date)
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Http exposing (..)
import Json.Decode as Decode exposing (field)
import Debug exposing (log)
import Json.Encode
import Validate exposing (ifBlank, ifNotInt, Validator,validate)


main =
  Html.program
    { init = init
    , view = view
    , update = update
    , subscriptions = subscriptions
    }


-- MODEL


type alias Model =
  { records : List Record
  , currentRecord : CurrentData
  , errors : List Error
  , bmi : Float
  , result : String
  }
type alias CurrentData =
  { age : Int
  , height : Int
  , weight : Int
  , gender : Int
  }
type alias Record =
  { age : Int
  , height : Int
  , weight : Int
  , gender : String
  , index : Float
  , result : String
  , date : String
  }

type Gender = Female | Male

type FormField = Age | Height | Weight

type alias Error = ( FormField, String )



init : (Model, Cmd Msg)
init  =
  ( Model [] (CurrentData 0 0 0 0) [] 0.0 ""
  , Cmd.none
  )

-- UPDATE

type Msg
  = SendNewRequestToBack
  | GetResponseFromBack (Result Http.Error (List Record))
  | ChangeAge String
  | ChangeHeight String
  | ChangeWeight String
  | SwitchTo Gender


update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    ChangeAge age ->
        let
            num = Result.withDefault 0 (String.toInt age)
            curRec= model.currentRecord
        in
            ({model| currentRecord =
                        CurrentData num curRec.height curRec.weight curRec.gender}, Cmd.none)
    SwitchTo newGender ->
        let
            num = if newGender == Male then 1 else if newGender == Female then 0 else -1
            curRec= model.currentRecord
        in
            ({model| currentRecord =
                         CurrentData curRec.age curRec.height curRec.weight num}, Cmd.none)
    ChangeHeight hgt ->
        let
            num = Result.withDefault 0 (String.toInt hgt)
            curRec= model.currentRecord
        in
            ({model| currentRecord =
                        CurrentData curRec.age num curRec.weight curRec.gender}, Cmd.none)

    ChangeWeight wgt ->
         let
            num = Result.withDefault 0 (String.toInt wgt)
            curRec= model.currentRecord
         in
            ({model| currentRecord =
                        CurrentData curRec.age curRec.height num curRec.gender}, Cmd.none)

    SendNewRequestToBack ->
        case valid model.currentRecord of
        [] ->
            ({model | errors = []}, getResponse model.currentRecord)
        errors ->
            ( { model | errors = errors }
                        , Cmd.none
                        )

    GetResponseFromBack (Ok newRecordFromResp) ->
        let
            bmI = List.head (List.reverse newRecordFromResp)
        in
            case bmI of
            Just rec ->
                ({model| records = newRecordFromResp, bmi = rec.index, result = rec.result}, Cmd.none)
            Nothing ->
                (model,Cmd.none)

    GetResponseFromBack (Err _) ->
      (model, Cmd.none)


valid : CurrentData -> List Error
valid curDat =
    if (curDat.height <= 0 && curDat.weight <=0)
     then [ (Height,"must be integer, more than 0"),(Weight,"must be integer, more than 0") ]
     else
        if (curDat.height <= 0)
        then [ (Height,"cant be < = 0")]
        else
            if curDat.weight <=0
            then [ (Weight,"cant be < = 0")]
            else []

-- VIEW

view : Model -> Html Msg
view model =

    div[][
        div [style [ ("float", "left") ]]
            [ h2 [] [text "BMI"]
            , div [blocStyle]
                [input [ type_ "text", placeholder "Age", onInput ChangeAge ] []
                , viewFormErrors Age model.errors
                ]
            ,div [blocStyle]
                [ fieldset []
                    [ radio (SwitchTo Male) "Male"
                    , radio (SwitchTo Female) "Female"
                    ]
                ]
            , div [blocStyle]
                [
                input [ type_ "text", placeholder "Height", onInput ChangeHeight ] []
                , viewFormErrors Height model.errors
                ]
            , div[blocStyle]
                [
                input [ type_ "text", placeholder "Weight", onInput ChangeWeight ] []
                , viewFormErrors Weight model.errors
                ]
            , div [blocStyle]
                [
                button [ onClick SendNewRequestToBack ] [ text "calc BmI" ]
                ]
            ]
        ,div[style [ ("float", "left")
                    ,("margin-left","15%")
                    ,("height", "15%")
                    ]]
            [h3 [] [text "Your BM index is:" ]
            ,if model.bmi /= 0 then h3 [] [text ((toString model.bmi)++" this is " ++ model.result )] else h3 [] []
            ]
        , br[] []
        ,div[style [("float", "left")
                   ,("margin-top","25%")
                   ]]
            [
             Html.table[]
                (List.concat [
                        [ thead []
                            [ th [][text "Age"]
                            , th [][text "Height"]
                            , th [][text "Weight"]
                            , th [][text "Gender"]
                            , th [][text "BMI"]
                            , th [][text "Result"]
                            , th [][text "Date"]
                            ]
                        ],
                        List.map toTableRow model.records
                    ])
            ]
        ]
blocStyle : Attribute msg
blocStyle =
  style
    [ ("height", "3%")
    , ("width", "172px")
    , ("margin-bottom","5px" )
    ]
toTableRow: Record -> Html Msg
toTableRow myListItem =
  tr []
     [ td[][text (toString myListItem.age)]
     , td[][text (toString myListItem.height)]
     , td[][text (toString myListItem.weight)]
     , td[][text myListItem.gender]
     , td[][text (toString myListItem.index)]
     , td[][text myListItem.result]
     , td[][text myListItem.date]
     ]

radio : msg -> String -> Html msg
radio msg name =
  label []
    [ input [ type_ "radio", onClick msg ] []
    , text name
    ]

viewFormErrors : FormField -> List Error -> Html msg
viewFormErrors field errors =
    errors
        |> List.filter (\( fieldError, _ ) -> fieldError == field)
        |> List.map (\( _, error ) -> li [] [ text error ])
        |> ul [ class "formErrors" ]

-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions model =
  Sub.none


-- HTTP


getResponse : CurrentData -> Cmd Msg
getResponse record =
           { body = recordEncoded record |> Http.jsonBody
           , expect = Http.expectJson listOfRecordsDecoder
           , headers = [ Http.header "Content-Type"  "application/json" ]
           , method = "POST"
           , timeout = Nothing
           , url = "http://localhost:8080/bmi"
           , withCredentials = False
           }
             |> Http.request
             |> Http.send GetResponseFromBack

-- coders
listOfRecordsDecoder : Decode.Decoder (List Record)
listOfRecordsDecoder =
                    Decode.list recordDecoder

recordDecoder : Decode.Decoder Record
recordDecoder =
    Decode.map7 Record
        (field "age" Decode.int)
        (field "height" Decode.int)
        (field "weight" Decode.int)
        (field "gender" Decode.string)
        (field "index" Decode.float)
        (field "result" Decode.string)
        (field "date" Decode.string)

recordEncoded : CurrentData -> Json.Encode.Value
recordEncoded data =
    let
        list =
            [ ( "age", Json.Encode.int data.age )
            , ( "height", Json.Encode.int data.height )
            , ( "weight", Json.Encode.int data.weight )
            , ( "gender", Json.Encode.int data.gender )
            ]
    in
        list
            |> Json.Encode.object

