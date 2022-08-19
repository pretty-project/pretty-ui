
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.keypress-handler.events
    (:require [mid-fruits.candy  :refer [return]]
              [mid-fruits.map    :refer [dissoc-in]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :refer [r]]))



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
  (assoc-in db [:environment :keypress-handler/data-items event-id] event-props))

(defn remove-event-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id]]
  (dissoc-in db [:environment :keypress-handler/data-items event-id]))

(defn empty-cache!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (dissoc-in db [:environment :keypress-handler/meta-items :cache]))

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
  ;   :on-keyup (metamorphic-event)(opt)}
  ;
  ; @return (map)
  [db [_ event-id {:keys [key-code on-keydown on-keyup]}]]
  ; XXX#1160 A regisztrált keypress események újra regisztrálásakor azok azonosítói nem ismétlődnek a cache vektorban
  (cond-> db on-keydown (update-in [:environment :keypress-handler/meta-items :cache key-code :keydown-events]
                                   vector/conj-item-once event-id)
             on-keyup   (update-in [:environment :keypress-handler/meta-items :cache key-code :keyup-events]
                                   vector/conj-item-once event-id)))

(defn uncache-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id]]
  (let [key-code (get-in db [:environment :keypress-handler/data-items event-id :key-code])]
       (-> db (update-in [:environment :keypress-handler/meta-items :cache key-code :keydown-events]
                         vector/remove-item event-id)
              (update-in [:environment :keypress-handler/meta-items :cache key-code :keyup-events]
                         vector/remove-item event-id))))

(defn rebuild-cache!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (letfn [(f [db event-id event-props]
             (r cache-event! db event-id event-props))]
         (let [keypress-events (get-in db [:environment :keypress-handler/data-items])]
              (reduce-kv f (r empty-cache! db) keypress-events))))

(defn reg-keypress-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ; @param (map) event-props
  ;  {:required? (boolean)(opt)}
  ;
  ; @return (map)
  [db [_ event-id {:keys [required?] :as event-props}]]
  ; Az egyes keypress események regisztrálásakor a reg-keypress-event! függvény ...
  ; ... eltárolja az esemény tulajdonságait.
  ; ... hozzáadja az eseményt a cache-hez, ha a kezelő nincs {:type-mode? true} állapotban
  ;     vagy az esemény {:required? true} beállítással rendelkezik.
  (let [type-mode? (get-in db [:environment :keypress-handler/meta-items :type-mode?])]
       (cond-> db :store-event-props!             (as-> % (r store-event-props! % event-id event-props))
                  (or required? (not type-mode?)) (as-> % (r cache-event!       % event-id event-props)))))

(defn remove-keypress-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) event-id
  ;
  ; @return (map)
  [db [_ event-id event-props]]
  (as-> db % (r uncache-event!      % event-id)
             (r remove-event-props! % event-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn quit-type-mode!
  ; @usage
  ;  (r environment/quit-type-mode! db)
  ;
  ; @return (map)
  [db _]
  (as-> db % (r rebuild-cache! %)
             (assoc-in % [:environment :keypress-handler/meta-items :type-mode?] false)))

(defn set-type-mode!
  ; @usage
  ;  (r environment/set-type-mode! db)
  ;
  ; @return (map)
  [db _]
  (letfn [(f [db event-id {:keys [required?] :as event-props}]
             (if required? (r cache-event! db event-id event-props)
                           (return         db)))]
         (let [keypress-events (get-in db [:environment :keypress-handler/data-items])]
              (as-> db % (reduce-kv f (r empty-cache! %) keypress-events)
                         (assoc-in % [:environment :keypress-handler/meta-items :type-mode?] true)))))
