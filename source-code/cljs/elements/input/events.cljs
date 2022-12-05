
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.input.events
    (:require [candy.api :refer [return]]
              [map.api   :refer [dissoc-in]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-backup-value!
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
  ; Az use-initial-options! függvény csak abban az esetben alkalmazza
  ; az initial-options értékét, ha az options-path útvonalon tárolt érték még üres!
  (let [options (get-in db options-path)]
       (cond-> db (and      initial-options (empty? options))
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
  ; Az use-initial-value! függvény csak abban az esetben alkalmazza
  ; az initial-value értékét, ha a value-path útvonalon tárolt érték még üres!
  (let [stored-value (get-in db value-path)]
       (cond-> db (and      initial-value (nil? stored-value))
                  (assoc-in value-path initial-value))))

(defn mark-as-focused!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (assoc-in db [:elements :element-handler/meta-items input-id :focused?] true))

(defn mark-as-blurred!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (dissoc-in db [:elements :element-handler/meta-items input-id :focused?]))

(defn mark-as-visited!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (assoc-in db [:elements :element-handler/meta-items input-id :visited?] true))

(defn unmark-as-visited!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (dissoc-in db [:elements :element-handler/meta-items input-id :visited?]))

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
  [db [_ input-id {:keys [value-path]} value]]
  (assoc-in db value-path value))
