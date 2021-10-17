
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.22
; Description:
; Version: v0.8.8
; Compatibility: x4.3.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.editor
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.logical   :refer [nonfalse?]]
              [mid-fruits.map       :as map]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.api   :as elements]
              [x.app-sync.api       :as sync]
              [x.app-ui.api         :as ui]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name {:edit-original? true}
;  A {:value-path ...} tulajdonságként átadott Re-Frame adatbázis útvonalon tárolt
;  érték szerkesztése közben, az aktuális érték az útvonalra íródik.
;
; @name {:on-save ...}
;  A mentés gomb megnyomásakor megtörténő esemény, amely használatával
;  egyedi mentési eljárás is megvalósítható.



;; -- Descriptions ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @description
;  A szerkesztő az [:x.app-tools.editor/edit! ...] esemény meghívásával indítható.



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- editor-props->field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {:edit-path (item-path vector)}
  ;
  ; @param (map)
  ;  {:auto-focus? (boolean)
  ;   :value-path (item-path vector)}
  [editor-id {:keys [edit-path] :as editor-props}]
  (merge (map/inherit editor-props [:label :validator])
         {:auto-focus? true
          :value-path  edit-path}))



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
  ;   :primary-button-label (metamorphic-content)(opt)
  ;   :required? (boolean)
  ;   :value-path (item-path vector)}
  [editor-id {:keys [edit-original? value-path] :as editor-props}]
  (merge {:primary-button-label :save!
          :required?  true
          :edit-path  (db/path ::editors editor-id :value)
          :value-path (db/path ::editors editor-id :value)}
         (if edit-original? {:edit-path value-path})
         (param editor-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-editor-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (get-in db (db/path ::editors editor-id)))

(defn- get-editor-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (keyword) prop-id
  ;
  ; @return (*)
  [db [_ editor-id prop-id]]
  (get-in db (db/path ::editors editor-id prop-id)))

(defn get-editor-value
  ; @param (keyword) editor-id
  ;
  ; @return (string)
  [db [_ editor-id]]
  (let [edit-path (r get-editor-prop db editor-id :edit-path)]
       (get-in db edit-path)))

(defn- disable-save-button?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [field-value (r elements/get-input-value db ::editor-field)
        validator   (r get-editor-prop          db editor-id :validator)]
       (boolean (or (and (some? validator)
                         (not ((:f validator) field-value)))
                    (and (r get-editor-prop       db editor-id :required?)
                         (r elements/field-empty? db ::editor-field))))))

(defn- get-label-bar-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  ;  {:disable-save-button? (boolean)}
  [db [_ editor-id]]
  (merge (r get-editor-props db editor-id)
         {:disable-save-button? (r disable-save-button? db editor-id)}))

(a/reg-sub :x.app-tools.editor/get-label-bar-view-props get-label-bar-view-props)

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (r get-editor-props db editor-id))

(a/reg-sub :x.app-tools.editor/get-view-props get-view-props)



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
  (assoc-in db (db/path ::editors editor-id)
               (param editor-props)))

(defn- use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [initial-value (r get-editor-prop db editor-id :initial-value)
        edit-path     (r get-editor-prop db editor-id :edit-path)]
       (assoc-in db edit-path initial-value)))

(defn- use-original-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [value-path     (r get-editor-prop db editor-id :value-path)
        original-value (get-in db value-path)
        edit-path      (r get-editor-prop db editor-id :edit-path)]
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
  (let [edit-original?      (r get-editor-prop db editor-id :edit-original?)
        use-initial-value?  (some? initial-value)
        use-original-value? (and (not edit-original?)
                                 (not use-initial-value?))]
       (cond-> db :store-editor-props!        (store-editor-props! [event-id editor-id editor-props])
                  (param use-initial-value?)  (use-initial-value!  [event-id editor-id])
                  (param use-original-value?) (use-original-value! [event-id editor-id]))))

(a/reg-event-db :x.app-tools.editor/initialize! initialize!)

(defn- save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (map)
  [db [_ editor-id]]
  (let [edit-original? (r get-editor-prop db editor-id :edit-original?)]
       (if (not edit-original?)
           (let [value      (r get-editor-prop db editor-id :value)
                 value-path (r get-editor-prop db editor-id :value-path)]
                (assoc-in db value-path value))
           (return db))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.app-tools.editor/cancel-editing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [_ [_ editor-id]]
      {:dispatch-n [[:x.app-ui/close-popup!       editor-id]
                    [:x.app-elements/reset-input! ::text-field]]}))

(a/reg-event-fx
  :x.app-tools.editor/save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db         (r save-value!        db editor-id)
       :dispatch-n [(r get-editor-prop   db editor-id :on-save)
                    [:x.app-ui/close-popup! editor-id]]}))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- label-bar-save-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) view-props
  ;  {:disable-save-button? (boolean)
  ;   :primary-button-label (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [editor-id {:keys [disable-save-button? primary-button-label] :as x}]
  [elements/button {:disabled? disable-save-button?
                    :keypress  {:key-code 13 :required? true}
                    :on-click  [:x.app-tools.editor/save-value! editor-id]
                    :label     primary-button-label
                    :preset    :close-button}])

(defn- label-bar-cancel-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [editor-id _]
  [elements/button {:color    :default
                    :keypress {:key-code 27 :required? true}
                    :label    :cancel!
                    :preset   :close-button
                    :on-click [:x.app-tools.editor/cancel-editing! editor-id]}])

(defn- label-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [editor-id view-props]
  [elements/polarity {:start-content [label-bar-cancel-button editor-id view-props]
                      :end-content   [label-bar-save-button   editor-id view-props]}])

(defn- editor-helper
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) view-props
  ;  {:helper (metamorphic-content)(opt)}
  ;
  ; @return (component)
  [editor-id {:keys [helper]}]
  (if (some? helper)
      [:<> [elements/separator {:horizontal :horizontal :size :l}]
           [elements/text      {:content helper}]]))

(defn- view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) view-props
  ;
  ; @return (component)
  [editor-id view-props]
  (let [field-props (editor-props->field-props editor-id view-props)]
       [:<> [elements/separator  {:horizontal :horizontal :size :l}]
            [elements/text-field ::editor-field field-props]
            [editor-helper       editor-id view-props]
            [elements/separator  {:horizontal :horizontal :size :l}]]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :x.app-tools.editor/edit!
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
  ;   :primary-button-label (metamorphic-content)(opt)
  ;    Default: :save!
  ;   :required? (boolean)(opt)
  ;    Default: true
  ;   :validator (map)(opt)(constant)
  ;    {:f (function)
  ;     :invalid-message (metamorphic-content)}
  ;   :value-path (item-path vector)(opt)}
  ;
  ; @usage
  ;  [:x.app-tools.editor/edit! {...}]
  ;
  ; @usage
  ;  [:x.app-tools.editor/edit! :my-editor {...}]
  (fn [_ event-vector]
    (let [editor-id    (a/event-vector->second-id   event-vector)
          editor-props (a/event-vector->first-props event-vector)
          editor-props (a/prot editor-id editor-props editor-props-prototype)]
         {:dispatch-n [[:x.app-tools.editor/initialize! editor-id editor-props]
                       [:x.app-tools.editor/render!     editor-id editor-props]]})))

(a/reg-event-fx :x.app-tools.editor/render!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  (fn [_ [_ editor-id _]]
      [:x.app-ui/add-popup!
        editor-id
        {:content    #'view
         :layout     :boxed
         :label-bar  {:content    #'label-bar
                      :subscriber [:x.app-tools.editor/get-label-bar-view-props editor-id]}
         :subscriber [:x.app-tools.editor/get-view-props editor-id]}]))
