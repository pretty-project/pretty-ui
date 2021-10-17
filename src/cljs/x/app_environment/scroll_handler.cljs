
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.5.2
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.scroll-handler
    (:require [app-fruits.dom   :as dom]
              [mid-fruits.candy :refer [param return]]
              [mid-fruits.math  :as math]
              [x.app-core.api   :as a :refer [r]]
              [x.app-db.api     :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def scroll-listener
     #(a/dispatch-once 250 [::->scrolled]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-scroll-direction
  ; @return (keyword or nil)
  ;  XXX#0088
  ;  nil, :ttb, :btt
  [db _]
  (get-in db (db/meta-item-path ::primary :scroll-direction)))

(defn get-scroll-y
  ; @return (integer)
  [db _]
  (get-in db (db/meta-item-path ::primary :scroll-y)))

(defn scrolled-to-top?
  ; @return (boolean)
  [db _]
  (let [scroll-y (r get-scroll-y db)]
       (= scroll-y 0)))

(defn scrolled-to-bottom?
  ; @return (boolean)
  [db _]
  (let [scroll-y (r get-scroll-y db)]
       ; TODO ...
       (return nil)))

(defn scroll-direction-btt?
  ; @return (boolean)
  [db _]
  (= (r get-scroll-direction db) :btt))

(defn scroll-direction-ttb?
  ; @return (boolean)
  [db _]
  (= (r get-scroll-direction db) :ttb))

(defn scroll-direction-changed?
  ; @param (keyword)(opt) scroll-direction
  ;  XXX#0088
  ;
  ; @return (boolean)
  [db [_ scroll-direction]]
       ;  XXX#0088
  (and (some? scroll-direction)
       (not= scroll-direction (r get-scroll-direction db))))

(defn get-scroll-turn-position
  ; @return (integer)
  [db _]
  (get-in db (db/meta-item-path ::primary :scroll-turn-position)))

(defn get-scroll-turn-distance
  ; @return (integer)
  [db _]
  (let [scroll-turn-position (r get-scroll-turn-position db)
        scroll-turn-distance (- scroll-turn-position (dom/get-scroll-y))]
       (math/absolute scroll-turn-distance)))

(defn get-scroll-progress
  ; @return (integer)
  [db _]
  (get-in db (db/meta-item-path ::primary :scroll-progress)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- update-scroll-direction!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (let [stored-scroll-y  (get-in db (db/meta-item-path ::primary :scroll-y))
        scroll-direction (dom/get-scroll-direction stored-scroll-y)]
                  ; *
       (cond-> db (some? scroll-direction)
                  (assoc-in (db/meta-item-path ::primary :scroll-direction)
                            (param scroll-direction))
                  ; *
                  (r scroll-direction-changed? db scroll-direction)
                  (assoc-in (db/meta-item-path ::primary :scroll-turn-position)
                            (param stored-scroll-y)))))

(defn- update-scroll-position!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (let [scroll-y (dom/get-scroll-y)]
       (assoc-in db (db/meta-item-path ::primary :scroll-y)
                    (param scroll-y))))

(defn- update-scroll-progress!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (let [scroll-progress (dom/get-scroll-progress)]
       (assoc-in db (db/meta-item-path ::primary :scroll-progress)
                    (param scroll-progress))))

(defn- update-scroll-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db [event-id]]
  (-> db ; 1.
         (update-scroll-direction! [event-id])
         ; 2.
         (update-scroll-position!  [event-id])
         ; 3.
         (update-scroll-progress!  [event-id])))

(a/reg-event-db ::update-scroll-data! update-scroll-data!)



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::->scrolled
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n [[:x.app-environment.position-handler/update-stored-positions!]
                [::update-scroll-data!]]})



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-handled-fx
  ::set-scroll-y!
  ; @param (n) integer
  ;
  ; @usage
  ; [:x.app-environment.scroll-handler/set-scroll-y! 100]
  (fn [[n]] (dom/set-scroll-y! n)))

(a/reg-handled-fx
  ::scroll-to-top!
  ; @param (n) integer
  ;
  ; @usage
  ; [:x.app-environment.scroll-handler/scroll-to-top!]
  (fn [] (dom/set-scroll-y! 0)))

(a/reg-handled-fx
  ::scroll-to-element-top!
  ; @param (string) element-id
  ; @param (integer)(opt) offset
  ;
  ; @usage
  ; [:x.app-environment.scroll-handler/scroll-to-element-top! "my-element" 50]
  (fn [[element-id offset]]
      (dom/scroll-to-element-top! (dom/get-element-by-id element-id)
                                  (param offset))))

(a/reg-handled-fx
  ::listen-to-scroll!
  #(dom/add-event-listener! "scroll" scroll-listener))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ::initialize!
  {:dispatch-n [[::listen-to-scroll!]
                [::update-scroll-data!]]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [::initialize!]})
