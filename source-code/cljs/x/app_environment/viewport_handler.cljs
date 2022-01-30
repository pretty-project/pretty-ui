
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.5.2
; Compatibility: x4.5.8



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

(defn- resize-listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (function)
  []
  (a/dispatch-once 250 [:environment/->viewport-resized]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-viewport-height
  ; @usage
  ;  (r environment/get-viewport-height db)
  ;
  ; @return (integer)
  [db _]
  (get-in db (db/meta-item-path :environment/viewport-data :viewport-height)))

(defn get-viewport-width
  ; @usage
  ;  (r environment/get-viewport-width db)
  ;
  ; @return (integer)
  [db _]
  (get-in db (db/meta-item-path :environment/viewport-data :viewport-width)))

(defn get-viewport-profile
  ; XXX#6408
  ;
  ; @usage
  ;  (r environment/get-viewport-profile db)
  ;
  ; @return (keyword)
  ;  :xs, :s, :m, :l, :xl
  [db _]
  (get-in db (db/meta-item-path :environment/viewport-data :viewport-profile)))

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

(defn viewport-small?
  ; @usage
  ;  (r environment/viewport-small? db)
  ;
  ; @return (boolean)
  [db _]
  (r viewport-profiles-match? db [:xxs :xs :s]))

(defn viewport-medium?
  ; @usage
  ;  (r environment/viewport-medium? db)
  ;
  ; @return (boolean)
  [db _]
  (r viewport-profile-match? db :m))

(defn viewport-large?
  ; @usage
  ;  (r environment/viewport-large? db)
  ;
  ; @return (boolean)
  [db _]
  (r viewport-profiles-match? db [:l :xl :xxl]))

(defn get-viewport-orientation
  ; @usage
  ;  (r environment/get-viewport-orientation db)
  ;
  ; @return (keyword)
  ;  :landscape, :portrait
  [db _]
  (get-in db (db/meta-item-path :environment/viewport-data :viewport-orientation)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- update-viewport-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  [db _]
  (assoc-in db (db/meta-item-path :environment/viewport-data)
               {:viewport-height      (dom/get-viewport-height)
                :viewport-orientation (dom/get-viewport-orientation)
                :viewport-profile     (dom/get-viewport-profile)
                :viewport-width       (dom/get-viewport-width)}))

(a/reg-event-db :environment/update-viewport-data! update-viewport-data!)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- listen-to-viewport-resize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (dom/add-event-listener! "resize" resize-listener))

(a/reg-handled-fx :environment/listen-to-viewport-resize! listen-to-viewport-resize!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/detect-viewport-profile!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      (let [viewport-profile (dom/get-viewport-profile)]
           {:environment/set-element-attribute! ["x-body-container" "data-viewport-profile"
                                                 (keyword/to-string viewport-profile)]})))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/->viewport-resized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      {:db       (r update-viewport-data! db)
       :dispatch [:environment/detect-viewport-profile!]}))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init {:dispatch-n [[:environment/update-viewport-data!]
                              [:environment/detect-viewport-profile!]
                              [:environment/listen-to-viewport-resize!]]}})
