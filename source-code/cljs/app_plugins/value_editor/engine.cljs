
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.17
; Description:
; Version: v0.2.8
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.value-editor.engine
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ;
  ; @return (item-path vector)
  [editor-id]
  (db/path ::editors editor-id :value))



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

(a/reg-event-db :value-editor/initialize! initialize!)

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
  :value-editor/cancel-editing!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [_ [_ editor-id]]
      {:dispatch-n [[:x.app-ui/close-popup!       editor-id]
                    [:x.app-elements/reset-input! :value-editor/editor-field]]}))

(a/reg-event-fx
  :value-editor/save-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db          (r save-value!       db editor-id)
       :dispatch-n [(r get-editor-prop   db editor-id :on-save)
                    [:x.app-ui/close-popup! editor-id]]}))

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
  ;  [:value-editor/load! {...}]
  ;
  ; @usage
  ;  [:value-editor/load! :my-editor {...}]
  (fn [_ event-vector]
    (let [editor-id    (a/event-vector->second-id   event-vector)
          editor-props (a/event-vector->first-props event-vector)
          editor-props (a/prot editor-id editor-props editor-props-prototype)]
         {:dispatch-n [[:value-editor/initialize! editor-id editor-props]
                       [:value-editor/render!     editor-id editor-props]]})))
