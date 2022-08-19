
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.params
    (:require [mid.re-frame.types :as types]
              [mid-fruits.candy   :refer [return]]
              [mid-fruits.map     :as map]
              [mid-fruits.vector  :as vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event-vector<-params
  ; @param (event-vector) n
  ; @param (list of *) params
  ;
  ; @example
  ;  (re-frame/event-vector<-params [:my-event] "My param" "Your param")
  ;  =>
  ;  [:my-event "My param" "Your param"]
  ;
  ; @return (event-vector)
  [n & params]
  (vector/concat-items n params))

(defn metamorphic-event<-params
  ; @param (metamorphic-event) n
  ; @param (list of *) params
  ;
  ; @example
  ;  (re-frame/metamorphic-event<-params [:my-event] "My param" "Your param")
  ;  =>
  ;  [:my-event "My param" "Your param"]
  ;
  ; @usage
  ;  (re-frame/metamorphic-event<-params {:dispatch [:my-event]} "My param" "Your param")
  ;  =>
  ;  {:dispatch [:my-event "My param" "Your param"]}
  ;
  ; @return (metamorphic-event)
  [n & params]
  ; Szükséges megkülönböztetni az esemény vektort a dispatch-later és dispatch-tick esemény csoport vektortól!
  ; [:my-event ...]
  ; [{:ms 500 :dispatch [:my-event ...]}]
  (cond (types/event-vector? n) (vector/concat-items n params)
        (map?                n) (map/->values        n #(apply metamorphic-event<-params % params))
        :else                   (return              n)))
