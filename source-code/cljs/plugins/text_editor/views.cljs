
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
              [re-frame.api                   :as r]
              [x.app-elements.api             :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :text-editor/hack-9910
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} [_ editor-id {:keys [value-path]}]]
      ; XXX#9910
      (let [editor-content (helpers/get-editor-content editor-id)
            stored-value   (get-in db value-path)]
           (if-not (= editor-content stored-value)
                   {:fx [:text-editor/update-editor-difference! editor-id stored-value]}))))

(defn- hack-9910
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [editor-id {:keys [value-path] :as editor-props}]
  ; XXX#9910
  ; Ha a value-path Re-Frame adatbázis útvonalon tárolt érték megváltozik,
  ; akkor a hack-9910 Reagent komponens meghívja a [:text-editor/hack-9910 ...]
  ; eseményt, ami kiértékeli a változást ...
  (let [stored-value @(r/subscribe [:db/get-item value-path])]
       (r/dispatch [:text-editor/hack-9910 editor-id editor-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn jodit
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  [editor-id editor-props]
  [:> JoditEditor (helpers/jodit-attributes editor-id editor-props)])

(defn- text-editor-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {}
  [_ {:keys [info-text label required?]}]
  (if label [elements/label {:info-text info-text
                             :content   label
                             :required? required?}]))

(defn- text-editor-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {}
  [editor-id editor-props]
  [:div [:style {:type "text/css"}
                ".jodit-wysiwyg {background-color: var( --fill-color ); cursor: text}"]
        [text-editor-label editor-id editor-props]
        [jodit             editor-id editor-props]
        [hack-9910         editor-id editor-props]])

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
  ;   :info-text (metamorphic-content)(opt)
  ;   :insert-as (keyword)(opt)
  ;    :cleared-html, :html, :only-text, :plain-text
  ;    Default: :cleared-html
  ;   :label (metamorphic-content)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    Default: :write-something!
  ;   :required? (boolean)(opt)
  ;    Default: false
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
