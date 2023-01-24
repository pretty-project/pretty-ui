
(ns jodit.views
    (:require [jodit-react      :default JoditEditor]
              [jodit.attributes :as attributes]
              [random.api       :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @ignore
;
; @constant (string)
(def STYLE "
.p-jodit .jodit-wysiwyg {
 background-color: var( --fill-color-default );
 cursor:           text;
}")

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- jodit
  ; @ignore
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [editor-id editor-props]
  [:div {:class :p-jodit}
        [:style {:type "text/css"} STYLE]
        [:> JoditEditor (attributes/jodit-attributes editor-id editor-props)]])

(defn plugin
  ; @param (keyword)(opt) editor-id
  ; @param (map) editor-props
  ; {:autofocus? (boolean)(opt)
  ;   Default: false
  ;  :buttons (keywords in vector)(opt)
  ;   [:bold, :italic, :underline, :font, :font-size, :cut, :copy, :paste
  ;    :link, :undo, :redo, :brush]
  ;   Default: [:bold :italic :underline :brush]
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :insert-as (keyword)(opt)
  ;   :cleared-html, :html, :only-text, :plain-text
  ;   Default: :cleared-html
  ;  :min-height (px)(opt)
  ;  :on-blur (function)(opt)
  ;  :on-change (function)(opt)
  ;  :on-focus (function)(opt)
  ;  :placeholder (metamorphic-content)(opt)
  ;   Default: :write-something!
  ;  :update-trigger (keyword or string)(opt)
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
  ; [plugin :my-editor {:on-blur   on-blur-f
  ;                     :on-focus  on-focus-f
  ;                     :on-change on-change-f}]
  ([editor-props]
   [body (random/generate-keyword) editor-props])

  ([editor-id editor-props]
   [jodit editor-id editor-props]))
