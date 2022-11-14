
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.text-field.events
    (:require [candy.api              :refer [return]]
              [elements.input.events  :as input.events]
              [elements.input.helpers :as input.helpers]
              [elements.input.subs    :as input.subs]
              [mid-fruits.map         :refer [dissoc-in]]
              [mid-fruits.string      :as string]
              [re-frame.api           :as r :refer [r]]
              [x.db.api               :as x.db]
              [x.environment.api      :as x.environment]))



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
  ; BUG#6071
  ; Ha az on-type-ended megörténésekor alkalmazott show-surface! függvény
  ; alkalmazásának idején már egy másik mező van fókuszált állapotban, akkor
  ; az elhagyott mező surface felületét nem szabad megjeleníteni!
  (if (r input.subs/input-focused? db field-id)
      (assoc-in db [:elements :element-handler/field-surface] field-id)
      (return   db)))

(defn hide-surface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db _]
  (dissoc-in db [:elements :element-handler/field-surface]))



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
           (r x.db/set-vector-item! db value-path field-value)
           (r x.db/set-item!        db value-path field-value))))



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
             (r x.environment/quit-type-mode! %)))

(defn field-focused
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {}
  ;
  ; @return (map)
  [db [_ field-id {:keys [autofocus?] :as field-props}]]
  (let [visited? (r input.subs/input-visited? db field-id)]
       (as-> db % (r input.events/mark-as-focused! % field-id field-props)
                  (r x.environment/set-type-mode!  %)
                  ; Az autofocus használatakor nem szükséges lenyitni a surface felületet!
                  (if (and autofocus? (not visited?))
                      (return          %)
                      (r show-surface! % field-id field-props)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.text-field/show-surface! show-surface!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-event-db :elements.text-field/clear-value! clear-value!)
