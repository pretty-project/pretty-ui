
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.input.events
    (:require [mid-fruits.candy              :refer [return]]
              [mid-fruits.map                :refer [dissoc-in]]
              [x.app-core.api                :as a :refer [r]]
              [x.app-db.api                  :as db]
              [x.app-elements.engine.element :as element]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-backup-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (if-let [value-path (r element/get-element-prop db input-id :value-path)]
          (assoc-in db (db/path :elements/primary input-id :backup-value)
                       (get-in db value-path))
          (return db)))



; new (old) version
(defn use-initial-value!_
  [db [_ input-id]]
  ; - Ha a value-path útvonalon található bármilyen érték, akkor NEM kerül felülírásra!
  ; - Az initial-value értéke lehet false, ezért szükséges a (some? ...) függvénnyel vizsgálni!
  (let [value-path     (get-in db [:elements/primary :data-items input-id :value-path])
        initial-value  (get-in db [:elements/primary :data-items input-id :initial-value])
        original-value (get-in db value-path)]
       (if (and (nil?  original-value)
                (some? initial-value))
           (assoc-in db value-path initial-value)
           (return   db))))





; XXX#NEW VERSION!
(defn use-initial-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:initial-options (vector)(opt)
  ;   :options-path (vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [initial-options options-path]}]]
  ; Az use-initial-options! függvény csak abban az esetben alkalmazza
  ; az initial-options értékét, ha az options-path útvonalon tárolt érték még üres!
  (let [options (get-in db options-path)]
       (cond-> db (and      initial-options (empty? options))
                  (assoc-in options-path initial-options))))

; XXX#NEW VERSION!
(defn use-initial-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:initial-value (*)(opt)
  ;   :value-path (vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [initial-value value-path]}]]
  ; Az use-initial-value! függvény csak abban az esetben alkalmazza
  ; az initial-value értékét, ha a value-path útvonalon tárolt érték még üres!
  (let [value (get-in db value-path)]
       (cond-> db (and      initial-value (empty? value))
                  (assoc-in value-path initial-value))))


; XXX#NEW VERSION!
(defn mark-as-focused!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (assoc-in db [:elements :element-handler/meta-items input-id :focused?] true))

; XXX#NEW VERSION!
(defn mark-as-blurred!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (dissoc-in db [:elements :element-handler/meta-items input-id :focused?]))

; XXX#NEW VERSION!
(defn mark-as-visited!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (assoc-in db [:elements :element-handler/meta-items input-id :visited?] true))

; XXX#NEW VERSION!
(defn unmark-as-visited!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (dissoc-in db [:elements :element-handler/meta-items input-id :visited?]))

(defn init-input!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ;
  ; @return (map)
  [db [_ input-id]]
  (as-> db % ; Már nem igy van használva: lsd. select, radio-button, ...
             ;(r use-input-initial-value!  % input-id)
             (r store-backup-value! % input-id)

             ; HACK#1411
             ; Gyors hack, ami azt oldja meg, hogy az input validator ne validáljon addig
             ; amig az input nem kapta meg az initial-value értékét. Különben ha egy text-field
             ; validator-ja azt figyeli mondjuk, hogy üres-e a field, akkor egy pillanatra
             ; megjelenik a warning-message, mielött a field megkapja az initial-value értékét.
             ; A text-field újraírása után lehet, hogy erre a hack-re már nem is lesz szükség!
             (r element/set-element-prop! % input-id :initialized? true)))

(a/reg-event-db :elements/init-input! init-input!)

; XXX#NEW VERSION!
(defn reset-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:value-path (vector)}
  ;
  ; @return (map)
  [db [_ input-id {:keys [value-path]}]]
  (let [backup-value (get-in db [:elements :element-handler/meta-items input-id :backup-value])]
       (assoc-in db value-path backup-value)))

; XXX#NEW VERSION!
(defn clear-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (map) input-props
  ;  {:value-path (vector)}
  ;
  ; @return (map)
  [db [_ _ {:keys [value-path]}]]
  (dissoc-in db value-path))

(defn set-value!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) value
  ;
  ; @return (map)
  [db [_ input-id value]]
  (let [value-path (r element/get-element-prop db input-id :value-path)]
       (assoc-in db value-path value)))
