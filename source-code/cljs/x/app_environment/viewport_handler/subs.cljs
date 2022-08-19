
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.viewport-handler.subs
    (:require [dom.api           :as dom]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-viewport-height
  ; @usage
  ;  (r environment/get-viewport-height db)
  ;
  ; @return (integer)
  [db _]
  (get-in db [:environment :viewport-handler/meta-items :viewport-height]))

(defn get-viewport-width
  ; @usage
  ;  (r environment/get-viewport-width db)
  ;
  ; @return (integer)
  [db _]
  (get-in db [:environment :viewport-handler/meta-items :viewport-width]))

(defn get-viewport-profile
  ; XXX#6408
  ;
  ; @usage
  ;  (r environment/get-viewport-profile db)
  ;
  ; @return (keyword)
  ;  :xs, :s, :m, :l, :xl
  [db _]
  (get-in db [:environment :viewport-handler/meta-items :viewport-profile]))

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
  (get-in db [:environment :viewport-handler/meta-items :viewport-orientation]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/get-viewport-height]
;(a/reg-sub :environment/get-viewport-height get-viewport-height)

; @usage
;  [:environment/get-viewport-width]
;(a/reg-sub :environment/get-viewport-width get-viewport-width)

; @usage
;  [:environment/get-viewport-profile]
;(a/reg-sub :environment/get-viewport-profile get-viewport-profile)

; @usage
;  [:environment/viewport-profile-match?]
;(a/reg-sub :environment/viewport-profile-match? viewport-profile-match?)

; @usage
;  [:environment/viewport-profiles-match?]
;(a/reg-sub :environment/viewport-profiles-match? viewport-profiles-match?)

; @usage
;  [:environment/viewport-small?]
(a/reg-sub :environment/viewport-small? viewport-small?)

; @usage
;  [:environment/viewport-medium?]
(a/reg-sub :environment/viewport-medium? viewport-medium?)

; @usage
;  [:environment/viewport-large?]
(a/reg-sub :environment/viewport-large? viewport-large?)

; @usage
;  [:environment/get-viewport-orientation]
;(a/reg-sub :environment/get-viewport-orientation get-viewport-orientation)
