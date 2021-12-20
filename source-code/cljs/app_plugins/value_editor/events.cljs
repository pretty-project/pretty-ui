
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
              [app-plugins.value-editor.subs :as subs]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)(opt)
  ;   :value-path (item-path vector)(opt)}
  ;
  ; @return (map)
  ;  {:edit-path (item-path vector)
  ;   :required? (boolean)
  ;   :save-button-label (metamorphic-content)(opt)
  ;   :value-path (item-path vector)}
  [editor-id {:keys [edit-original? value-path] :as editor-props}]
  (merge {:required?         true
          :save-button-label :save!
          :edit-path  (subs/default-value-path editor-id)
          :value-path (subs/default-value-path editor-id)}
         (if edit-original? {:edit-path value-path})
         (param editor-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- store-editor-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;
  ; @return (map)
  [db [_ editor-id editor-props]]
  (assoc-in db (db/path ::value-editors editor-id)
               (param editor-props)))

(defn- use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [initial-value (r subs/get-editor-prop db editor-id :initial-value)
        edit-path     (r subs/get-editor-prop db editor-id :edit-path)]
       (assoc-in db edit-path initial-value)))

(defn- use-original-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [value-path     (r subs/get-editor-prop db editor-id :value-path)
        edit-path      (r subs/get-editor-prop db editor-id :edit-path)
        original-value (get-in db value-path)]
       (assoc-in db edit-path original-value)))

(defn- initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-original? (boolean)
  ;   :value-path (item-path vector)}
  ;
  ; @return (map)
  [db [event-id editor-id {:keys [edit-original? initial-value value-path] :as editor-props}]]
  (let [edit-original?      (r subs/get-editor-prop db editor-id :edit-original?)
        use-initial-value?  (some? initial-value)
        ; Ha nem az eredeti érték elérési útvonalán történik a szerkesztés, tehát az edit-path
        ; értéke nem egyenlő a value-path értékével és nincs alkalmazva initial-value érték,
        ; akkor a value-path útvonalon található érték lesz a szerkesztő mező kezdő értéke.
        use-original-value? (and (not edit-original?)
                                 (not use-initial-value?))]
       (cond-> db :store-editor-props!        (store-editor-props! [event-id editor-id editor-props])
                  (param use-initial-value?)  (use-initial-value!  [event-id editor-id])
                  (param use-original-value?) (use-original-value! [event-id editor-id]))))

(defn- save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [edit-original? (r subs/get-editor-prop db editor-id :edit-original?)]
       (if (not edit-original?)
           (let [value      (r subs/get-editor-prop db editor-id :value)
                 value-path (r subs/get-editor-prop db editor-id :value-path)]
                (assoc-in db value-path value))
           (return db))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :value-editor/cancel-editing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
       ; TODO ...
       ; Leírni, miért szükséges a reset-input-value! függvény végrehajtása!
       ; Minden esetben szükséges a végrehajtás? edit-original? true és false esetén is?
      {:db       (r elements/reset-input-value! db :value-editor/editor-field)
       :dispatch [:ui/close-popup! editor-id]}))

(a/reg-event-fx
  :value-editor/save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db (r save-value! db editor-id)
       :dispatch-n [(r subs/get-editor-prop db editor-id :on-save)
                    [:ui/close-popup!          editor-id]]}))

(a/reg-event-fx :value-editor/load!
  ; @param (keyword)(opt) editor-id
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
  ;   :value-path (item-path vector)(opt)}
  ;
  ; @usage
  ;  [:value-editor/load! {...}]
  ;
  ; @usage
  ;  [:value-editor/load! :my-editor {...}]
  (fn [{:keys [db]} event-vector]
    (let [editor-id    (a/event-vector->second-id   event-vector)
          editor-props (a/event-vector->first-props event-vector)
          editor-props (a/prot editor-id editor-props editor-props-prototype)]
         {:db       (r initialize!      db editor-id editor-props)
          :dispatch [:value-editor/render! editor-id editor-props]})))
