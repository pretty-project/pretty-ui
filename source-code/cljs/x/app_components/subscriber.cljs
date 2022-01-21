
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.23
; Description:
; Version: v0.8.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.subscriber
    (:require [mid-fruits.candy :refer [param]]
              [mid-fruits.map   :as map]
              [x.app-core.api   :as a]
              [x.app-components.transmitter :rename {component transmitter}]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#7081 ----------------------------------------------------------------

; @name subscriber
;  A {:subscriber [...]} tulajdonságként átadott Re-Frame subscription vektor
;  használatával a subscriber komponens feliratkozik a subscription visszatérési
;  értékére, és azt {:subscribed-props {...}} térképként a XXX#0069 logika
;  szerint átadja a komponensnek.
;
; @name initial-props
;  XXX#0001
;
; @name base-props
;  XXX#0001
;
; @name modifier
;  XXX#0001
;
; @name static-props
;  XXX#0001



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :initial-props (map)(opt)
  ;   :modifier (function)(opt)
  ;   :render-f (function)
  ;   :static-props (map)(opt)
  ;   :subscriber (subscription-vector)
  ;    Return value must be a map!}
  ;
  ; @usage
  ;  [components/subscriber {...}]
  ;
  ; @usage
  ;  [components/subscriber :my-component {...}]
  ;
  ; @usage
  ;  (defn my-component [component-id dynamic-props])
  ;  [components/subscriber {:render-f   #'my-component
  ;                          :subscriber [:get-my-props]}]
  ;
  ; @usage
  ;  (defn my-component [component-id static-props dynamic-props])
  ;  [components/subscriber {:render-f     #'my-component
  ;                          :static-props {...}
  ;                          :subscriber   [:get-my-props]}]
  ;
  ; @return (*)
  ([context-props]
   [component (a/id) context-props])

  ([component-id {:keys [subscriber] :as context-props}]
   (let [subscribed-props (a/subscribe subscriber)]
        (fn [_ context-props]
            (let [context-props (assoc context-props :subscribed-props @subscribed-props)]
                 [transmitter component-id context-props])))))
