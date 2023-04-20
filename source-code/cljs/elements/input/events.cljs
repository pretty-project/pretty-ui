
(ns elements.input.events
    (:require [elements.input.subs :as input.subs]
              [map.api             :refer [dissoc-in]]
              [noop.api            :refer [return]]
              [re-frame.api        :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn backup-stored-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:value-path (Re-Frame path vector)}
  ;
  ; @return (map)
  [db [_ input-id {:keys [value-path]}]]
  (assoc-in db [:elements :element-handler/backup-values input-id]
               (get-in db value-path)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn use-initial-options!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:initial-options (vector)(opt)
  ;  :options-path (Re-Frame path vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [initial-options options-path]}]]
  ; The 'use-initial-options!' function stores the initial options of an element
  ; in the application state, but only if no options has been set.
  (let [options (get-in db options-path)]
       (cond-> db (and initial-options (empty? options))
                  (assoc-in options-path initial-options))))

(defn use-initial-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:initial-value (*)(opt)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @return (map)
  [db [_ input-id {:keys [initial-value value-path] :as input-props}]]
  ; The 'use-initial-value!' function stores the initial value of an element
  ; in the application state, but only if no value has been set.
  (cond-> db (and initial-value (r input.subs/input-empty? db input-id input-props))
             (assoc-in value-path initial-value)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reset-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:value-path (Re-Frame path vector)}
  ;
  ; @return (map)
  [db [_ input-id {:keys [value-path]}]]
  (let [backup-value (get-in db [:elements :element-handler/backup-values input-id])]
       (assoc-in db value-path backup-value)))

(defn clear-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:value-path (Re-Frame path vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]}]]
  (dissoc-in db value-path))

(defn set-value!
  ; @ignore
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ; {:value-path (Re-Frame path vector)}
  ; @param (*) value
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]} value]]
  (assoc-in db value-path value))
