
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.17
; Description:
; Version: v0.7.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.events
    (:require [mid-fruits.candy   :refer [param return]]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-elements.api :as elements]
              [app-plugins.value-editor.engine :as engine]
              [app-plugins.value-editor.subs   :as subs]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)(opt)
  ;   :value-path (item-path vector)}
  ;
  ; @return (map)
  ;  {:edit-path (item-path vector)
  ;   :required? (boolean)
  ;   :save-button-label (metamorphic-content)(opt)}
  [extension-id editor-id {:keys [edit-original? value-path] :as editor-props}]
  (merge {:required?         true
          :save-button-label :save!
          :edit-path (engine/default-edit-path extension-id editor-id)}
         (if edit-original? {:edit-path value-path})
         (param editor-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-editor-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [_ extension-id editor-id editor-props]]
  (assoc-in db [extension-id :value-editor/meta-items editor-id] editor-props))

(defn- use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ extension-id editor-id]]
  (let [initial-value (r subs/get-meta-value db extension-id editor-id :initial-value)
        edit-path     (r subs/get-meta-value db extension-id editor-id :edit-path)]
       (assoc-in db edit-path initial-value)))

(defn- use-original-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ extension-id editor-id]]
  (let [value-path     (r subs/get-meta-value db extension-id editor-id :value-path)
        edit-path      (r subs/get-meta-value db extension-id editor-id :edit-path)
        original-value (get-in db value-path)]
       (assoc-in db edit-path original-value)))

(defn- initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)
  ;   :value-path (item-path vector)}
  ;
  ; @return (map)
  [db [event-id extension-id editor-id {:keys [edit-original? initial-value value-path] :as editor-props}]]
  (let [edit-original?      (r subs/get-meta-value db extension-id editor-id :edit-original?)
        use-initial-value?  (some? initial-value)
        ; Ha nem az eredeti érték elérési útvonalán történik a szerkesztés, tehát az edit-path
        ; értéke nem egyenlő a value-path értékével és nincs alkalmazva initial-value érték,
        ; akkor a value-path útvonalon található érték lesz a szerkesztő mező kezdeti értéke.
        use-original-value? (and (not edit-original?)
                                 (not use-initial-value?))]
       (cond-> db :store-editor-props!        (store-editor-props! [event-id extension-id editor-id editor-props])
                  (param use-initial-value?)  (use-initial-value!  [event-id extension-id editor-id])
                  (param use-original-value?) (use-original-value! [event-id extension-id editor-id]))))

(defn- save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ extension-id editor-id]]
  (let [edit-original? (r subs/get-meta-value db extension-id editor-id :edit-original?)]
       (if (not edit-original?)
           (let [value      (r subs/get-meta-value db extension-id editor-id :value)
                 value-path (r subs/get-meta-value db extension-id editor-id :value-path)]
                (assoc-in db value-path value))
           (return db))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :value-editor/cancel-editing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ extension-id editor-id]]
       ; TODO ...
       ; Leírni, miért szükséges a reset-input-value! függvény végrehajtása!
       ; Minden esetben szükséges a végrehajtás? edit-original? true és false esetén is?
      {:db       (r elements/reset-input-value! db :value-editor/editor-field)
       :dispatch [:ui/close-popup! (engine/popup-id extension-id editor-id)]}))

(a/reg-event-fx
  :value-editor/save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ extension-id editor-id]]
      {:db (r save-value! db extension-id editor-id)
       :dispatch-n [(r subs/get-meta-value db extension-id editor-id :on-save)
                    [:ui/close-popup! (engine/popup-id extension-id editor-id)]]}))

(a/reg-event-fx :value-editor/load!
  ; @param (keyword) extension-id
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)(opt)
  ;    Default: false
  ;    Only w/ {:value-path [...]}
  ;   :helper (metamorphic-content)(opt)
  ;   :initial-value (string)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Default: false
  ;   :on-save (metamorphic-event)(opt)
  ;   :required? (boolean)(opt)
  ;    Default: true
  ;   :save-button-label (metamorphic-content)(opt)
  ;    Default: :save!
  ;   :validator (map)(opt)(constant)
  ;    {:f (function)
  ;     :invalid-message (metamorphic-content)}
  ;   :value-path (item-path vector)}
  ;
  ; @usage
  ;  [:value-editor/load! :my-extension :my-editor {...}]
  (fn [{:keys [db]} [_ extension-id editor-id editor-props]]
    (let [editor-props (editor-props-prototype extension-id editor-id editor-props)]
         {:db       (r initialize!      db extension-id editor-id editor-props)
          :dispatch [:value-editor/render! extension-id editor-id]})))
