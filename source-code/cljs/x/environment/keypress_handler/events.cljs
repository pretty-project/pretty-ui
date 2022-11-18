
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.keypress-handler.events
    (:require [candy.api    :refer [return]]
              [map.api      :refer [dissoc-in]]
              [re-frame.api :as r :refer [r]]
              [vector.api   :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-event-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ; @param (map) event-props
  ;
  ; @return (map)
  [db [_ event-id event-props]]
  ; XXX#1160 A regisztrált keypress események újra regisztrálásakor azok tulajdonságai felülíródnak
  (assoc-in db [:x.environment :keypress-handler/data-items event-id] event-props))

(defn remove-event-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id]]
  (dissoc-in db [:x.environment :keypress-handler/data-items event-id]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-cache!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db [:x.environment :keypress-handler/meta-items :cache]))

(defn cache-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; A keypress események regisztrálásakor az egyes események azonosítói egy cache vektorba
  ; kerülnek, így egy adott billentyű lenyomáskor kisebb számításigénnyel lehetséges
  ; a billentyűkódhoz tartozó eseményeket elérni az adatbázisból
  ;
  ; @param (keyword) event-id
  ; @param (map) event-props
  ;  {:key-code (integer)
  ;   :on-keydown (metamorphic-event)(opt)
  ;   :on-keyup (metamorphic-event)(opt)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (map)
  [db [_ event-id {:keys [key-code on-keydown on-keyup required?]}]]
  ; XXX#1160 A regisztrált keypress események újra regisztrálásakor azok azonosítói nem ismétlődnek a cache vektorban
  ;
  ; Csak abban az esetben adja hozzá az eseményt a cache-hez, ha a kezelő nincs {:type-mode? true} állapotban,
  ; vagy abban van, de az esemény {:required? true} beállítással rendelkezik.

  (let [type-mode? (get-in db [:x.environment :keypress-handler/meta-items :type-mode?])]
       (if (or required? (not type-mode?))
           (cond-> db on-keydown (update-in [:x.environment :keypress-handler/meta-items :cache key-code :keydown-events]
                                            vector/conj-item-once event-id)
                      on-keyup   (update-in [:x.environment :keypress-handler/meta-items :cache key-code :keyup-events]
                                            vector/conj-item-once event-id))
           (return db))))

(defn uncache-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id]]
  (let [key-code (get-in db [:x.environment :keypress-handler/data-items event-id :key-code])]
       (-> db (update-in [:x.environment :keypress-handler/meta-items :cache key-code :keydown-events]
                         vector/remove-item event-id)
              (update-in [:x.environment :keypress-handler/meta-items :cache key-code :keyup-events]
                         vector/remove-item event-id))))

(defn rebuild-cache!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (letfn [(f [db event-id event-props]
             (r cache-event! db event-id event-props))]
         (let [keypress-events (get-in db [:x.environment :keypress-handler/data-items])]
              (reduce-kv f (r empty-cache! db) keypress-events))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-exclusivity!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ; @param (map) event-props
  ;  {}
  ;
  ; @return (map)
  [db [_ event-id {:keys [exclusive? key-code] :as event-props}]]
  (if exclusive? (-> db (update-in [:x.environment :keypress-handler/meta-items :exclusivity key-code] vector/conj-item event-id)
                        (dissoc-in [:x.environment :keypress-handler/meta-items :cache       key-code]))
                 (return db))
  (return db))

(defn unset-exclusivity!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id]]
  (let [key-code (get-in db [:x.environment :keypress-handler/data-items event-id :key-code])]
       (-> db (update-in [:x.environment :keypress-handler/meta-items :exclusivity key-code] vector/remove-item event-id)))
              ;(as-> % (let [exclusivity (get-in db [:x.environment :keypress-handler/meta-items key-code])]
              ;             (if (vector/nonempty? exclusivity)
              ;                 (let [most-exclusive (last exclusivity)])))))
  (return db))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-keypress-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ; @param (map) event-props
  ;
  ; @return (map)
  [db [_ event-id event-props]]
  ; Az egyes keypress események regisztrálásakor a reg-keypress-event! függvény ...
  ; ... eltárolja az esemény tulajdonságait.
  ; ...
  ; ... hozzáadja az eseményt a cache-hez.
  (as-> db % (r store-event-props! % event-id event-props)
             (r set-exclusivity!   % event-id event-props)
             (r cache-event!       % event-id event-props)))

(defn remove-keypress-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id event-props]]
  (as-> db % (r uncache-event!      % event-id)
             (r unset-exclusivity!  % event-id)
             (r remove-event-props! % event-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-type-mode!
  ; @usage
  ;  (r quit-type-mode! db)
  ;
  ; @return (map)
  [db _]
  (as-> db % (r rebuild-cache! %)
             (assoc-in % [:x.environment :keypress-handler/meta-items :type-mode?] false)))

(defn set-type-mode!
  ; @usage
  ;  (r set-type-mode! db)
  ;
  ; @return (map)
  [db _]
  (letfn [(f [db event-id {:keys [required?] :as event-props}]
             (if required? (r cache-event! db event-id event-props)
                           (return         db)))]
         (let [keypress-events (get-in db [:x.environment :keypress-handler/data-items])]
              (as-> db % (reduce-kv f (r empty-cache! %) keypress-events)
                         (assoc-in % [:x.environment :keypress-handler/meta-items :type-mode?] true)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.environment/quit-type-mode!]
(r/reg-event-db :x.environment/quit-type-mode! quit-type-mode!)

; @usage
;  [:x.environment/set-type-mode!]
(r/reg-event-db :x.environment/set-type-mode!  set-type-mode!)