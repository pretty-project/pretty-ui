
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.text-editor.views
    (:require [mid-fruits.random              :as random]
              [plugins.text-editor.helpers    :as helpers]
              [plugins.text-editor.prototypes :as prototypes]
              [plugins.text-editor.state      :as state]
              [jodit-react                    :default JoditEditor]
              [x.app-core.api                 :as a]
              [x.app-elements.api             :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn jodit
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [editor-id editor-props]
  [:> JoditEditor (helpers/jodit-attributes editor-id editor-props)])

(defn- text-editor-body
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {}
  [editor-id editor-props]
  [:div [:style {:type "text/css"}
                ".jodit-wysiwyg {cursor: text}"]
        [jodit editor-id editor-props]])

(defn- text-editor
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:disabled? (boolean)(opt)
  ;   :indent (map)(opt)}
  [editor-id {:keys [disabled? indent] :as editor-props}]
  [elements/blank editor-id
                  {:content   [text-editor-body editor-id editor-props]
                   :class     :plugins--text-editor
                   :disabled? disabled?
                   :indent    indent}])

(defn body
  ; @param (keyword)(opt) editor-id
  ; @param (map) editor-props
  ;  {:autofocus? (boolean)(opt)
  ;    Default: false
  ;   :buttons (keywords in vector)(opt)
  ;    [:bold, :italic, :underline, :font, :font-size, :cut, :copy, :paste
  ;     :link, :undo, :redo]
  ;    Default: [:bold :italic :underline]
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;    A tulajdonság leírását a x.app-elements.api/blank dokumentációjában találod!
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [text-editor/body {...}]
  ;
  ; @usage
  ;  [text-editor/body :my-editor {...}]
  ([editor-props]
   [body (random/generate-keyword) editor-props])

  ([editor-id editor-props]
   (let [editor-props (prototypes/editor-props-prototype editor-id editor-props)]
        [text-editor editor-id editor-props])))
