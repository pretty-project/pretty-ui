
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
    (:require [mid-fruits.candy             :refer [param]]
              [mid-fruits.map               :as map]
              [x.app-components.transmitter :rename {component transmitter}]
              [x.app-core.api               :as a]))



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
;
; @name test-f
;  Ha a test-f paraméterként átadott függvény, (amelynek egyetlen paramétere
;  a subscribed-props érték) kimenete igaz, akkor megtörténik a komponens React-Fába
;  csatolása.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def SUBSRIBER-PROPS [:base-props :component :initial-props :static-props :subscriber :test-f])



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn extended-props->subscriber-props
  ; @param (map) extended-props
  ;
  ; @return (map)
  [extended-props]
  (map/inherit extended-props SUBSRIBER-PROPS))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :component (component)
  ;   :initial-props (map)(opt)
  ;   :modifier (function)(opt)
  ;   :static-props (map)(opt)
  ;   :subscriber (subscription vector)
  ;    Return value must be a map!
  ;   :test-f (function)(opt)}
  ;
  ; @usage
  ;  [components/subscriber {...}]
  ;
  ; @usage
  ;  [components/subscriber :my-component {...}]
  ;
  ; @usage
  ;  (defn my-component [component-id dynamic-props])
  ;  [components/subscriber {:component  #'my-component
  ;                          :subscriber [::get-view-props]}]
  ;
  ; @usage
  ;  (defn my-component [component-id static-props dynamic-props])
  ;  [components/subscriber {:component    #'my-component
  ;                          :test-f       map?
  ;                          :static-props {...}
  ;                          :subscriber   [::get-view-props]}]
  ;
  ; @return (*)
  ([context-props]
   [component (a/id) context-props])

  ([component-id {:keys [component initial-props subscriber test-f] :as context-props}]
   (let [subscribed-props (a/subscribe subscriber)]
        (fn [_ context-props]
            (if (or (nil?   test-f)
                    (test-f @subscribed-props))
                (let [context-props (assoc context-props :subscribed-props @subscribed-props)]
                     [transmitter component-id context-props]))))))
