
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.viewport-handler.subs
    (:require [dom.api      :as dom]
              [re-frame.api :as r :refer [r]]
              [vector.api   :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-viewport-height
  ; @usage
  ;  (r get-viewport-height db)
  ;
  ; @return (integer)
  [db _]
  (get-in db [:x.environment :viewport-handler/meta-items :viewport-height]))

(defn get-viewport-width
  ; @usage
  ;  (r get-viewport-width db)
  ;
  ; @return (integer)
  [db _]
  (get-in db [:x.environment :viewport-handler/meta-items :viewport-width]))

(defn get-viewport-profile
  ; XXX#6408
  ;
  ; @usage
  ;  (r get-viewport-profile db)
  ;
  ; @return (keyword)
  ;  :xs, :s, :m, :l, :xl
  [db _]
  (get-in db [:x.environment :viewport-handler/meta-items :viewport-profile]))

(defn viewport-profile-match?
  ; @param (keyword) n
  ;
  ; @usage
  ;  (r viewport-profile-match? :xl)
  ;
  ; @return (boolean)
  [db [_ n]]
  (= n (r get-viewport-profile db)))

(defn viewport-profiles-match?
  ; @param (vector) n
  ;
  ; @usage
  ;  (r viewport-profiles-match? [:xs :s :m])
  ;
  ; @return (boolean)
  [db [_ n]]
  (vector/contains-item? n (r get-viewport-profile db)))

(defn viewport-small?
  ; @usage
  ;  (r viewport-small? db)
  ;
  ; @return (boolean)
  [db _]
  (r viewport-profiles-match? db [:xxs :xs :s]))

(defn viewport-medium?
  ; @usage
  ;  (r viewport-medium? db)
  ;
  ; @return (boolean)
  [db _]
  (r viewport-profile-match? db :m))

(defn viewport-large?
  ; @usage
  ;  (r viewport-large? db)
  ;
  ; @return (boolean)
  [db _]
  (r viewport-profiles-match? db [:l :xl :xxl]))

(defn viewport-min?
  ; @param (px) min-width
  ;
  ; @usage
  ;  (r viewport-min? db 1024)
  ;
  ; @return (boolean)
  [db [_ min-width]]
  (<= min-width (r get-viewport-width db)))

(defn viewport-max?
  ; @param (px) max-width
  ;
  ; @usage
  ;  (r viewport-max? db 1024)
  ;
  ; @return (boolean)
  [db [_ max-width]]
  (>= max-width (r get-viewport-width db)))

(defn get-viewport-orientation
  ; @usage
  ;  (r get-viewport-orientation db)
  ;
  ; @return (keyword)
  ;  :landscape, :portrait
  [db _]
  (get-in db [:x.environment :viewport-handler/meta-items :viewport-orientation]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.environment/get-viewport-height]
(r/reg-sub :x.environment/get-viewport-height get-viewport-height)

; @usage
;  [:x.environment/get-viewport-width]
(r/reg-sub :x.environment/get-viewport-width get-viewport-width)

; @usage
;  [:x.environment/get-viewport-profile]
(r/reg-sub :x.environment/get-viewport-profile get-viewport-profile)

; @usage
;  [:x.environment/viewport-profile-match?]
;(r/reg-sub :x.environment/viewport-profile-match? viewport-profile-match?)

; @usage
;  [:x.environment/viewport-profiles-match?]
;(r/reg-sub :x.environment/viewport-profiles-match? viewport-profiles-match?)

; @usage
;  [:x.environment/viewport-small?]
(r/reg-sub :x.environment/viewport-small? viewport-small?)

; @usage
;  [:x.environment/viewport-medium?]
(r/reg-sub :x.environment/viewport-medium? viewport-medium?)

; @usage
;  [:x.environment/viewport-large?]
(r/reg-sub :x.environment/viewport-large? viewport-large?)

; @usage
;  [:x.environment/viewport-min? 1024]
(r/reg-sub :x.environment/viewport-min? viewport-min?)

; @usage
;  [:x.environment/viewport-max? 1024]
(r/reg-sub :x.environment/viewport-max? viewport-max?)

; @usage
;  [:x.environment/get-viewport-orientation]
(r/reg-sub :x.environment/get-viewport-orientation get-viewport-orientation)
