
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.text-field.events
    (:require [mid-fruits.candy             :refer [return]]
              [mid-fruits.map               :refer [dissoc-in]]
              [mid-fruits.string            :as string]
              [x.app-core.api               :as a :refer [r]]
              [x.app-db.api                 :as db]
              [x.app-elements.input.events  :as input.events]
              [x.app-elements.input.helpers :as input.helpers]
              [x.app-environment.api        :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (r input.events/use-initial-value! db field-id field-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn show-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (assoc-in db [:elements :element-handler/meta-items field-id :surface-visible?] true))

(defn hide-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (dissoc-in db [:elements :element-handler/meta-items field-id :surface-visible?]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (r input.events/clear-value! db field-id field-props))

(defn empty-field!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (map)
  [db [_ _ {:keys [field-value-f value-path]}]]
  (let [field-value (field-value-f string/empty-string)]
       (assoc-in db value-path field-value)))

(defn store-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ; @param (string) field-content
  ;
  ; @return (map)
  [db [_ _ {:keys [field-value-f value-path]} field-content]]
  (let [field-value (field-value-f field-content)]
       (if (input.helpers/value-path->vector-item? value-path)
           (r db/set-vector-item! db value-path field-value)
           (r db/set-item!        db value-path field-value))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn type-ended
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ; @param (string) field-content
  ;
  ; @return (map)
  [db [_ field-id field-props field-content]]
  (as-> db % (r show-surface! % field-id field-props)
             (r store-value!  % field-id field-props field-content)))

(defn field-blurred
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (as-> db % (r input.events/mark-as-blurred! % field-id field-props)
             (r input.events/mark-as-visited! % field-id field-props)
             (r hide-surface!                 % field-id)
             (r environment/quit-type-mode!   %)))

(defn field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (map)
  [db [_ field-id field-props]]
  (as-> db % (r input.events/mark-as-focused! % field-id field-props)
             (r show-surface!                 % field-id field-props)
             (r environment/set-type-mode!    %)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.text-field/show-surface! show-surface!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :elements.text-field/clear-value! clear-value!)
