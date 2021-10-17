
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.3.8
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.viewport-handler
    (:require [app-fruits.dom     :as dom]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.vector  :as vector]
              [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def resize-listener
     #(a/dispatch-once 250 [::->viewport-resized]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-viewport-height
  ; @return (integer)
  [db _]
  (get-in db (db/meta-item-path ::primary :viewport-height)))

(defn get-viewport-width
  ; @return (integer)
  [db _]
  (get-in db (db/meta-item-path ::primary :viewport-width)))

(defn get-viewport-profile
  ; XXX#6408
  ;
  ; @return (keyword)
  ;  :xs, :s, :m, :l, :xl
  [db _]
  (get-in db (db/meta-item-path ::primary :viewport-profile)))

(defn viewport-profile-match?
  ; @param (keyword) n
  ;
  ; @usage
  ;  (r environment/viewport-profile-match? :xl)
  ;
  ; @return (boolean)
  [db [_ n]]
  (= n (r get-viewport-profile db)))

(defn viewport-profiles-match?
  ; @param (vector) n
  ;
  ; @usage
  ;  (r environment/viewport-profiles-match? [:xs :s :m])
  ;
  ; @return (boolean)
  [db [_ n]]
  (vector/contains-item? n (r get-viewport-profile db)))

(defn get-viewport-orientation
  ; @return (keyword)
  ;  :landscape, :portrait
  [db _]
  (get-in db (db/meta-item-path ::primary :viewport-orientation)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- update-viewport-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db (db/meta-item-path ::primary)
               {:viewport-height      (dom/get-viewport-height)
                :viewport-orientation (dom/get-viewport-orientation)
                :viewport-profile     (dom/get-viewport-profile)
                :viewport-width       (dom/get-viewport-width)}))

(a/reg-event-db ::update-viewport-data! update-viewport-data!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::detect-viewport-profile!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      [:x.app-environment.element-handler/set-attribute!
       "x-body-container" "data-viewport-profile"
       (keyword/to-string (dom/get-viewport-profile))]))

(a/reg-handled-fx
  ::listen-to-viewport-resize!
  #(dom/add-event-listener! "resize" resize-listener))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::->viewport-resized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n
   [[:x.app-environment.position-handler/update-stored-positions!]
    [::update-viewport-data!]
    [::detect-viewport-profile!]]})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:dispatch-n
   [[::update-viewport-data!]
    [::detect-viewport-profile!]
    [::listen-to-viewport-resize!]]})

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [::initialize!]})
