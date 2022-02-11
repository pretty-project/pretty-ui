
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.27
; Description:
; Version: v0.8.2
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.engine.selectable
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.map       :as map]
              [mid-fruits.vector    :as vector]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]
              [x.app-db.api         :as db]
              [x.app-elements.engine.element :as element]
              [x.app-elements.engine.input   :as input]
              [x.app-environment.api         :as environment]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-select-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (function)
  [input-id option]
  #(a/dispatch [:elements/select-option! input-id option]))

(defn on-unselect-function
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (function)
  [input-id]
  #(a/dispatch [:elements/unselect-option! input-id]))

(defn selectable-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:selected? (boolean)}
  ;
  ; @return (map)
  ;  {:data-selected (boolean)}
  [input-id {:keys [selected?] :as input-props}]
  (element/element-attributes input-id input-props {:data-selected (boolean selected?)}))

(defn selectable-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:disabled? (boolean)(opt)
  ;   :value (keyword)(opt)}
  ; @param (*) option
  ;
  ; @return (map)
  ;  {:data-selected (boolean)
  ;   :disabled (boolean)
  ;   :on-click (function)}
  [input-id {:keys [disabled?] :as input-props} option]
  (let [selected-value (:value input-props)
        selected?      (= selected-value option)]
       (if disabled? {:data-selected (param selected?)
                      :disabled      (param true)}
                     {:data-selected (param selected?)
                      :on-click      (on-select-function input-id option)
                      :on-mouse-up  #(environment/blur-element!)})))

(defn selectable-unselect-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:selected? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-disabled (string)
  ;   :disabled (boolean)
  ;   :on-click (function)
  ;   :title (string)}
  [input-id {:keys [selected?]}]
  (if selected? {:on-click      (on-unselect-function input-id)
                 :on-mouse-up  #(environment/blur-element!)
                 :title         (components/content {:content :uncheck-selected!})}
                {:data-disabled (param true)
                 :disabled      (param true)}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selectable-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (vector)
  [db [_ input-id]]
  ; BUG#7633
  (if-let [options-path (r element/get-element-prop db input-id :options-path)]
          (get-in db options-path)))

(defn option-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (boolean)
  [db [_ input-id option]]
  (let [input-value (r input/get-input-value db input-id)]
       (= input-value option)))

(a/reg-sub :elements/option-selected? option-selected?)

(defn selectable-selected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (let [value (r input/get-input-value db input-id)]
       (not (nil? value))))

(defn selectable-nonselected?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (boolean)
  [db [_ input-id]]
  (not (r selectable-selected? db input-id)))

(defn get-selectable-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :helper (keyword)
  ;   :options (vector)
  ;   :selected? (boolean)
  ;   :value (keyword)}
  [db [_ input-id]]
  (merge {:options   (r get-selectable-options db input-id)
          :selected? (r selectable-selected?   db input-id)
          :value     (r input/get-input-value  db input-id)}
         (if (r input/input-required-warning? db input-id)
             {:border-color :warning
              :helper       :please-select-an-option})))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-selectable-initial-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (if-let [initial-options (r element/get-element-prop db input-id :initial-options)]
          (let [options-path (r element/get-element-prop db input-id :options-path)]
               (assoc-in db options-path initial-options))
          (return db)))

(defn- init-selectable!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (as-> db % (r input/init-input!               % input-id)
             (r use-selectable-initial-options! % input-id)))

(a/reg-event-db :elements/init-selectable! init-selectable!)

(defn- add-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) value
  ;
  ; @return (map)
  [db [_ input-id value]]
  (let [options-path (r element/get-element-prop db input-id :options-path)]
       (update-in db options-path vector/conj-item-once value)))

(a/reg-event-db :elements/add-option! add-option!)

(defn select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ input-id option]]
  (let [value-path  (r element/get-element-prop db input-id :value-path)
        get-value-f (r element/get-element-prop db input-id :get-value-f)
        value       (get-value-f option)]
       (as-> db % (r db/set-item!                 % value-path value)
                  (r input/mark-input-as-visited! % input-id))))

(defn unselect-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  [db [_ input-id]]
  (let [value-path (r element/get-element-prop db input-id :value-path)]
       (as-> db % (r db/remove-item!              % value-path)
                  (r input/mark-input-as-visited! % input-id))))

(a/reg-event-db :elements/unselect-option! unselect-option!)

(defn toggle-select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  ;
  ; @return (map)
  [db [_ input-id option]]
  (if (r option-selected? db input-id option)
      (r unselect-option! db input-id)
      (r select-option!   db input-id option)))

(a/reg-event-db :elements/toggle-select-option! toggle-select-option!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/select-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  (fn [{:keys [db]} [_ input-id option]]
      (let [on-select-event (r element/get-element-prop db input-id :on-select)
            get-value-f     (r element/get-element-prop db input-id :get-value-f)
            value           (get-value-f option)]
           {:db       (r select-option! db input-id option)
            :dispatch (a/metamorphic-event<-params on-select-event value)})))
