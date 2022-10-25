
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.text-editor.views
    (:require [engines.text-editor.helpers    :as helpers]
              [engines.text-editor.prototypes :as prototypes]
              [engines.text-editor.state      :as state]
              [mid-fruits.random              :as random]
              [plugins.ckeditor5.api          :as ckeditor5]
              [re-frame.api                   :as r]
              [reagent.api                    :as reagent]
              [x.app-elements.api             :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizer-debug
  [editor-id {:keys [value-path]}]
  ; HACK#9910
  (let [stored-value @(r/subscribe [:db/get-item value-path])]
       [:div [:br] "output:  " (get @engines.text-editor.state/EDITOR-OUTPUT  editor-id)
             [:br] "input:   " (get @engines.text-editor.state/EDITOR-INPUT   editor-id)
             [:br] "trigger: " (get @engines.text-editor.state/EDITOR-TRIGGER editor-id)
             [:br] "stored:  " stored-value]))

(defn synchronizer-sensor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [editor-id editor-props stored-value]
  ; HACK#9910
  (reagent/lifecycles {:component-will-unmount (fn [_ _ _] (helpers/synchronizer-will-unmount-f editor-id editor-props))
                       :component-did-mount    (fn [_ _ _] (helpers/synchronizer-did-mount-f    editor-id editor-props))
                       :component-did-update   (fn [%]     (helpers/synchronizer-did-update-f   editor-id %))
                       :reagent-render         (fn [_ _ _])}))

(defn- synchronizer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [editor-id {:keys [value-path] :as editor-props}]
  ; HACK#9910
  (let [stored-value @(r/subscribe [:db/get-item value-path])]
       [synchronizer-sensor editor-id editor-props stored-value]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- ckeditor5
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [editor-id editor-props]
  (let [ckeditor5-props (prototypes/ckeditor5-props-prototype editor-id editor-props)]
       [ckeditor5/body editor-id ckeditor5-props]))

(defn- text-editor-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {}
  [_ {:keys [info-text label required?]}]
  (if label [elements/label {:content   label
                             :info-text info-text
                             :required? required?}]))

(defn- text-editor-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {}
  [editor-id editor-props]
  [:<> [text-editor-label editor-id editor-props]
       [ckeditor5         editor-id editor-props]
      ;[synchronizer-debug editor-id editor-props]
       [synchronizer      editor-id editor-props]])

(defn- text-editor
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:disabled? (boolean)(opt)
  ;   :indent (map)(opt)}
  [editor-id {:keys [disabled? indent] :as editor-props}]
  [elements/blank editor-id
                  {:content   [text-editor-body editor-id editor-props]
                   :disabled? disabled?
                   :indent    indent}])

(defn body
  ; @param (keyword)(opt) editor-id
  ; @param (map) editor-props
  ;  {:autofocus? (boolean)(opt)
  ;    Default: false
  ;   :buttons (keywords in vector)(opt)
  ;    A tulajdonság leírását a plugins.ckeditor5.views/body dokumentációjában találod!
  ;    Default: [:bold :italic :underline :fontColor]
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;    A tulajdonság leírását a x.app-elements.api/blank dokumentációjában találod!
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    Default: :write-something!
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [body {...}]
  ;
  ; @usage
  ;  [body :my-editor {...}]
  ([editor-props]
   [body (random/generate-keyword) editor-props])

  ([editor-id editor-props]
   (let [editor-props (prototypes/editor-props-prototype editor-id editor-props)]
        [text-editor editor-id editor-props])))
