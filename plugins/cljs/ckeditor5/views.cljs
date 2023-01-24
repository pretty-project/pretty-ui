
(ns ckeditor5.views
    (:require ["@ckeditor/ckeditor5-react" :refer [CKEditor]]
              [ckeditor5.attributes        :as attributes]
              [random.api                  :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------
 
; @ignore
;
; @constant (string)
(def STYLE "
.p-ckeditor5 {
  display: flex;
  flex-direction: column-reverse;
}

.p-ckeditor5 .ck.ck-toolbar {
  border-color: var( --border-color-highlight );
  border-style: none none solid none;
  border-width: 1px;
  position:     sticky;
  top:          48px;

.p-ckeditor5 .ck.ck-editor__editable_inline,
.p-ckeditor5 .ck.ck-editor__editable:not(.ck-editor__nested-editable).ck-focused {
  border-color: var( --border-color-highlight );
  border-style: none solid solid solid;
  border-width: 1px;
  cursor:       text;
  font-size:    var( --font-size-s );
}

.p-ckeditor5 .ck.ck-editor__editable:not(.ck-editor__nested-editable).ck-focused {
  box-shadow: none
}

.p-ckeditor5 .ck strong {
  font-weight: 500;
}

.p-ckeditor5 .ck.ck-button {
  cursor: pointer
}")

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ckeditor5
  ; @ignore
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [editor-id editor-props]
  [:div {:class :p-ckeditor5}
        [:style {:type "text/css"} STYLE]
        [:> CKEditor (attributes/ckeditor-attributes editor-id editor-props)]])

(defn plugin
  ; @param (keyword)(opt) editor-id
  ; @param (map) editor-props
  ; {:autofocus? (boolean)(opt)
  ;   Default: false
  ;  :buttons (keywords in vector)(opt)
  ;   [:fontColor, :fontBackgroundColor, :heading, :bold, :italic, :underline, :link, ...]
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :font-colors (maps in vector)(opt)
  ;   [{:color (string)
  ;     :label (string)}]
  ;  :fill-colors (maps in vector)(opt)
  ;   [{:color (string)
  ;     :label (string)}]
  ;  :on-blur (function)(opt)
  ;  :on-change (function)(opt)
  ;  :on-focus (function)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;  :value (string)(opt)}
  ;
  ; @usage
  ; [plugin {...}]
  ;
  ; @usage
  ; [plugin :my-editor {...}]
  ;
  ; @usage
  ; (defn on-blur-f   [editor-id editor-props])
  ; (defn on-focus-f  [editor-id editor-props])
  ; (defn on-change-f [editor-id editor-props value])
  ; [body :my-editor {:on-blur   on-blur-f
  ;                   :on-focus  on-focus-f
  ;                   :on-change on-change-f}]
  ([editor-props]
   [plugin (random/generate-keyword) editor-props])

  ([editor-id editor-props]
   [ckeditor5 editor-id editor-props]))
