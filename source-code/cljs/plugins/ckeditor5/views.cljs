
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.ckeditor5.views
    (:require ["@ckeditor/ckeditor5-react" :refer [CKEditor]]
              [mid-fruits.css              :as css]
              [mid-fruits.random           :as random]
              [plugins.ckeditor5.helpers   :as helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ckeditor5
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [editor-id editor-props]
  [:<> [:style {:type "text/css"} ".ck.ck-toolbar {"
                                  (css/unparse {:border-color "var( --border-color-highlight )"
                                                :border-style "solid"
                                                :border-width "1px"})
                                  "}"
                                  ".ck.ck-editor__editable_inline,"
                                  ".ck.ck-editor__editable:not(.ck-editor__nested-editable).ck-focused {"
                                  (css/unparse {:border-color  "var( --border-color-highlight )"
                                                :border-style  "none solid solid solid"
                                                :border-width  "1px"
                                                :cursor        "text"
                                                :font-size     "var( --font-size-s )"})
                                  "}"
                                  ".ck.ck-editor__editable:not(.ck-editor__nested-editable).ck-focused {"
                                  (css/unparse {:box-shadow "none"})
                                  "}"
                                  ".ck strong {"
                                  (css/unparse {:font-weight "500"})
                                  "}"
                                  ".ck.ck-button {"
                                  (css/unparse {:cursor "pointer"})
                                  "}"]
       [:> CKEditor (helpers/ckeditor-attributes editor-id editor-props)]])

(defn body
  ; @param (keyword)(opt) editor-id
  ; @param (map) editor-props
  ;  {:autofocus? (boolean)(opt)
  ;    Default: false
  ;   :background-colors (maps in vector)(opt)
  ;    [{:color (string)
  ;      :label (string)}]
  ;   :buttons (keywords in vector)(opt)
  ;    [:fontColor, :fontBackgroundColor, :heading, :bold, :italic, :underline, :link, ...]
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-colors (maps in vector)(opt)
  ;    [{:color (string)
  ;      :label (string)}]
  ;   :on-blur (function)(opt)
  ;   :on-change (function)(opt)
  ;   :on-focus (function)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :value (string)(opt)}
  ;
  ; @usage
  ;  [ckeditor5/body {...}]
  ;
  ; @usage
  ;  [ckeditor5/body :my-editor {...}]
  ([editor-props]
   [body (random/generate-keyword) editor-props])

  ([editor-id editor-props]
   [ckeditor5 editor-id editor-props]))
