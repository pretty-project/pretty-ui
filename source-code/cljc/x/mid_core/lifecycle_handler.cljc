
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.23
; Description:
; Version: v1.2.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.lifecycle-handler
    (:require [mid-fruits.candy         :refer [param return]]
              [mid-fruits.map           :as map]
              [mid-fruits.random        :as random]
              [mid-fruits.string        :as string]
              [mid-fruits.vector        :as vector]
              [x.mid-core.event-handler :as event-handler :refer [r]]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name on-app-init
;  Clojure:        Az on-app-boot előtt lefutó események.
;  Clojure/Script: Az on-app-boot előtt lefutó események.
;
; @name on-app-boot
;  Clojure:        A szerver indítása előtt lefutó események.
;  Clojure/Script: Az applikáció első renderelése előtt lefutó események.
;
; @name on-app-launch
;  Clojure:        A szerver indítása után lefutó események.
;  Clojure/Script: Az applikáció első renderelése után lefutó események.
;
; @name on-login
;  Clojure/Script: (Nem vendég) bejelentkezéskor lefutó események.
;
; @name on-browser-online
;  Clojure/Script: Nomen est omen.
;
; @name on-browser-offline
;  Clojure/Script: Nomen est omen.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- generate-life-id
  ; @return (namespaced keyword)
  []
  (keyword (random/generate-string) "lifecycles"))

(defn- life-id->namespace
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword or namespaced keyword) life-id
  ;
  ; @return (string)
  [life-id]
  (let [namespace (namespace life-id)]
       (if (string/nonempty? namespace)
           (return namespace)
           (random/generate-string))))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-lifes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (get-in db [::lifes :data-items]))

(defn get-period-events
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) period-id
  ;
  ; @return (vector)
  [db [_ period-id]]
  (vec (reduce-kv #(if-let [period (get %3 period-id)]
                           (conj %1 period) %1)
                   (param [])
                   (r get-lifes db))))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-lifecycles!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) life-id
  ; @param (map) lifecycles
  ;
  ; @return (map)
  [db [_ life-id lifecycles]]
  (let [namespace (life-id->namespace life-id)]
       (assoc-in db [::lifes :data-items life-id]
                    (reduce-kv (fn [lifecycles period-id event]
                                   (let [event-id (keyword namespace (name period-id))]
                                        (event-handler/reg-event-fx event-id event)
                                        (assoc lifecycles period-id [event-id])))
                               (param {}) lifecycles))))

(defn reg-event!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) period-id
  ; @param (metamorphic-event) event
  ;
  ; @return (map)
  [db [_ period-id event]]
  (let [event-id (random/generate-keyword)
        life-id  (generate-life-id)]
       (event-handler/reg-event-fx event-id event)
       (update-in db [::lifes :data-items life-id period-id]
                  vector/conj-item event-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-lifecycles
  ; @param (namespaced keyword)(opt) life-id
  ; @param (map) lifecycles
  ;  {:on-app-init   (metamorphic-event)
  ;   :on-app-boot   (metamorphic-event)
  ;   :on-app-launch (metamorphic-event)
  ;   :on-login      (metamorphic-event)
  ;    (Only in Clojure/Script namespaces!)}
  ;
  ; @usage
  ;  (reg-lifecycles
  ;   :namespace/lifecycles
  ;   {...}
  ([lifecycles]
   (reg-lifecycles (generate-life-id) lifecycles))

  ([life-id lifecycles]
   (event-handler/db reg-lifecycles! [:x.mid-core/reg-lifecycles! life-id lifecycles])))
