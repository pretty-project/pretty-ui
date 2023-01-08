
(ns elements.input.events
    (:require [candy.api :refer [return]]
              [map.api   :refer [dissoc-in]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-stored-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:value-path (vector)}
  ;
  ; @return (map)
  [db [_ input-id {:keys [value-path]}]]
  (assoc-in db [:elements :element-handler/backup-values input-id]
               (get-in db value-path)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-initial-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:initial-options (vector)(opt)
  ;  :options-path (vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [initial-options options-path]}]]
  ; The 'use-initial-options!' function stores the initial options of an element
  ; in the application state, but only if no options have been set yet.
  (let [options (get-in db options-path)]
       (cond-> db (and initial-options (empty? options))
                  (assoc-in options-path initial-options))))

(defn use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:initial-value (*)(opt)
  ;  :value-path (vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [initial-value value-path]}]]
  ; The 'use-initial-value!' function stores the initial value of an element
  ; in the application state, but only if no value have been set yet.
  (let [stored-value (get-in db value-path)]
       (cond-> db (and initial-value (nil? stored-value))
                  (assoc-in value-path initial-value))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:value-path (vector)}
  ;
  ; @return (map)
  [db [_ input-id {:keys [value-path]}]]
  (let [backup-value (get-in db [:elements :element-handler/backup-values input-id])]
       (assoc-in db value-path backup-value)))

(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:value-path (vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]}]]
  (dissoc-in db value-path))

(defn set-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:value-path (vector)}
  ; @param (*) value
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]} value]]
  (assoc-in db value-path value))
