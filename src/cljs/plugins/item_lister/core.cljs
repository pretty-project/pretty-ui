(ns plugins.item-lister.core
  (:require
   [app-fruits.reagent :refer [ratom lifecycles]]
   [plugins.item-lister.engine :as engine]
   [plugins.item-lister.items :as items]
   [x.app-components.api :as components]
   [x.app-elements.api :as elements]
   [x.app-core.api :as a]))


(defn stated-item-lister [lister-id lister-props]
  [:<>
   [items/view  lister-id lister-props]])

(defn item-lister
  ; @param (keyword)(opt) lister-id
  ; @param (map) lister-props
  ;  {:element (component)
  ;   :on-order-change (metamorphic-event)(opt)
  ;   :value-path      (items-path vector)
  ;   :download-items-event (metamorphic-event)}
  ;
  ; @usage
  ;  [item-lister {...}]
  ;
  ; @usage
  ;  [item-lister :my-item-lister {...}]
  ;
  ; @usage
  ;  (defn my-element [lister-id item-dex item] [:div ...])
  ;  [item-lister :my-item-lister {:element    #'my-element
  ;                                :value-path [:my :items]}]
  ;
  ; @return (component)
  ([lister-props] [item-lister nil lister-props])
  ([lister-id lister-props]
   (let [lister-id (a/id lister-id)]
     [components/stated lister-id {:component           #'stated-item-lister
                                   :initial-props       lister-props
                                   :initial-props-path  (engine/lister-props-path lister-id)}])))




(def items
  [{:car/id "213"
    :car/name "Seat"
    :car/model "Cordoba"}
   {:car/id "76548"
    :car/name "Seat"
    :car/model "Ibiza"}
   {:car/id "543543"
    :car/name "Seat"
    :car/model "Toledo"}
   {:car/id "098"
    :car/name "Seat"
    :car/model "Arosa"}
   {:car/id "87683"
    :car/name "Seat"
    :car/model "Alhambra"}
   {:car/id "2654"
    :car/name "Seat"
    :car/model "Leon"}])

(a/reg-lifecycles
  {;:on-app-init
   :on-app-boot [:x.app-db/set-item! [:a-items] items]})

;:on-app-launch
;:on-app-login})

