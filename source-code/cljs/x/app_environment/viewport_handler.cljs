
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.22
; Description:
; Version: v0.6.8
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.viewport-handler
    (:require [app-fruits.dom    :as dom]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
              [x.app-environment.element-handler :as element-handler]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- resize-listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (function)
  []
  (a/dispatch-once 250 [:environment/viewport-resized]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-viewport-height
  ; @usage
  ;  (r environment/get-viewport-height db)
  ;
  ; @return (integer)
  [db _]
  (get-in db (db/meta-item-path :environment/viewport-data :viewport-height)))

; @usage
;  [:environment/get-viewport-height]
;(a/reg-sub :environment/get-viewport-height get-viewport-height)

(defn get-viewport-width
  ; @usage
  ;  (r environment/get-viewport-width db)
  ;
  ; @return (integer)
  [db _]
  (get-in db (db/meta-item-path :environment/viewport-data :viewport-width)))

; @usage
;  [:environment/get-viewport-width]
;(a/reg-sub :environment/get-viewport-width get-viewport-width)

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

; @usage
;  [:environment/get-viewport-profile]
;(a/reg-sub :environment/get-viewport-profile get-viewport-profile)

(defn viewport-profile-match?
  ; @param (keyword) n
  ;
  ; @usage
  ;  (r environment/viewport-profile-match? :xl)
  ;
  ; @return (boolean)
  [db [_ n]]
  (= n (r get-viewport-profile db)))

; @usage
;  [:environment/viewport-profile-match?]
;(a/reg-sub :environment/viewport-profile-match? viewport-profile-match?)

(defn viewport-profiles-match?
  ; @param (vector) n
  ;
  ; @usage
  ;  (r environment/viewport-profiles-match? [:xs :s :m])
  ;
  ; @return (boolean)
  [db [_ n]]
  (vector/contains-item? n (r get-viewport-profile db)))

; @usage
;  [:environment/viewport-profiles-match?]
;(a/reg-sub :environment/viewport-profiles-match? viewport-profiles-match?)

(defn viewport-small?
  ; @usage
  ;  (r environment/viewport-small? db)
  ;
  ; @return (boolean)
  [db _]
  (r viewport-profiles-match? db [:xxs :xs :s]))

; @usage
;  [:environment/viewport-small?]
(a/reg-sub :environment/viewport-small? viewport-small?)

(defn viewport-medium?
  ; @usage
  ;  (r environment/viewport-medium? db)
  ;
  ; @return (boolean)
  [db _]
  (r viewport-profile-match? db :m))

; @usage
;  [:environment/viewport-medium?]
(a/reg-sub :environment/viewport-medium? viewport-medium?)

(defn viewport-large?
  ; @usage
  ;  (r environment/viewport-large? db)
  ;
  ; @return (boolean)
  [db _]
  (r viewport-profiles-match? db [:l :xl :xxl]))

; @usage
;  [:environment/viewport-large?]
(a/reg-sub :environment/viewport-large? viewport-large?)

(defn get-viewport-orientation
  ; @usage
  ;  (r environment/get-viewport-orientation db)
  ;
  ; @return (keyword)
  ;  :landscape, :portrait
  [db _]
  (get-in db (db/meta-item-path :environment/viewport-data :viewport-orientation)))

; @usage
;  [:environment/get-viewport-orientation]
;(a/reg-sub :environment/get-viewport-orientation get-viewport-orientation)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- listen-to-viewport-resize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (dom/add-event-listener! "resize" resize-listener))

(a/reg-fx :environment/listen-to-viewport-resize! listen-to-viewport-resize!)

(defn- update-viewport-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (a/dispatch [:db/set-item! (db/meta-item-path :environment/viewport-data)
                             {:viewport-height      (dom/get-viewport-height)
                              :viewport-orientation (dom/get-viewport-orientation)
                              :viewport-profile     (dom/get-viewport-profile)
                              :viewport-width       (dom/get-viewport-width)}]))

(a/reg-fx :environment/update-viewport-data! update-viewport-data!)

(defn- detect-viewport-profile!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [viewport-profile (dom/get-viewport-profile)]
       (element-handler/set-element-attribute! "x-body-container" "data-viewport-profile" (name viewport-profile))))

(a/reg-fx :environment/detect-viewport-profile! detect-viewport-profile!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/viewport-resized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:fx-n [[:environment/detect-viewport-profile!]
          [:environment/update-viewport-data!]]})



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init {:fx-n [[:environment/detect-viewport-profile!]
                        [:environment/listen-to-viewport-resize!]
                        [:environment/update-viewport-data!]]}})
