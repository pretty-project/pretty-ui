
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

(defn init-field!
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
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (map)
  [db [_ field-id _]]
  (assoc-in db [:elements :element-handler/meta-items field-id :surface-visible?] true))

(defn hide-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (map)
  [db [_ field-id _]]
  (dissoc-in db [:elements :element-handler/meta-items field-id :surface-visible?]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn clear-field-value!
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
  [db [_ _ {:keys [value-path]}]]
  (assoc-in db value-path string/empty-string))

(defn store-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ; @param (string) value
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]} value]]
  (if (input.helpers/value-path->vector-item? value-path)
      (r db/set-vector-item! db value-path value)
      (r db/set-item!        db value-path value)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn type-ended
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ; @param (string) value
  ;
  ; @return (map)
  [db [_ field-id field-props value]]
  (as-> db % (r show-surface! % field-id field-props)
             (r store-value!  % field-id field-props value)))

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
             (r hide-surface!                 % field-id field-props)
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
(a/reg-event-db :elements.text-field/clear-field-value! clear-field-value!)
