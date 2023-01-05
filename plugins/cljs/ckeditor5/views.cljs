
(ns ckeditor5.views
    (:require ["@ckeditor/ckeditor5-react" :refer [CKEditor]]
              [ckeditor5.helpers           :as helpers]
              [css.api                     :as css]
              [random.api                  :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ckeditor5
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [editor-id editor-props]
  [:div {:style {:display "flex" :flex-direction "column-reverse"}}
        [:style {:type "text/css"} ".ck.ck-toolbar {"
                                   (css/unparse {:border-color "var( --border-color-highlight )"
                                                 :border-style "none none solid none"
                                                 :border-width "1px"
                                                 :position     "sticky"
                                                 :top          "48px"})
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
  ; {:autofocus? (boolean)(opt)
  ;   Default: false
  ;  :background-colors (maps in vector)(opt)
  ;   [{:color (string)
  ;     :label (string)}]
  ;  :buttons (keywords in vector)(opt)
  ;   [:fontColor, :fontBackgroundColor, :heading, :bold, :italic, :underline, :link, ...]
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :font-colors (maps in vector)(opt)
  ;   [{:color (string)
  ;     :label (string)}]
  ;  :on-blur (function)(opt)
  ;  :on-change (function)(opt)
  ;  :on-focus (function)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :value (string)(opt)}
  ;
  ; @usage
  ; [body {...}]
  ;
  ; @usage
  ; [body :my-editor {...}]
  ;
  ; @usage
  ; (defn on-blur-f   [editor-id editor-props])
  ; (defn on-focus-f  [editor-id editor-props])
  ; (defn on-change-f [editor-id editor-props value])
  ; [body :my-editor {:on-blur   on-blur-f
  ;                   :on-focus  on-focus-f
  ;                   :on-change on-change-f}]
  ([editor-props]
   [body (random/generate-keyword) editor-props])

  ([editor-id editor-props]
   [ckeditor5 editor-id editor-props]))
