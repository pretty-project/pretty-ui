
(ns plugins.item-lister.core
    (:require [app-fruits.reagent         :refer [ratom lifecycles]]
              [plugins.item-lister.engine :as engine]
              [plugins.item-lister.items  :as items]
              [x.app-components.api       :as components]
              [x.app-core.api             :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn item-lister
  ; @param (keyword)(opt) lister-id
  ; @param (map) lister-props
  ;  {:element (component)
  ;   :icon (keyword)(opt)
  ;   :on-list-ended (metamorphic-event)(opt)
  ;    Az elemeket megjelenítő lista végére érve megtörténő esemény
  ;   :on-order-change (metamorphic-event)(opt)
  ;    Az elemek sorrendjének megváltozásakor megtörténő esemény
  ;    Only w/ {:sortable? true}
  ;   :request-id (keyword)(opt)
  ;    Az elemek felsorolása alatt megjelenített request-indicator, milyen
  ;    azonosítójú request aktivitását jelezze vissza
  ;   :sortable? (boolean)(opt)
  ;    Default: false
  ;   :subscriber (subscription vector)(opt)
  ;    A feliratkozás visszatérési értékét a lista minden eleme egyaránt megkapja paraméterként.
  ;   :value-path (items-path vector)}
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
  ; @usage
  ;  (defn my-element [lister-id item-dex item common-props] [:div ...])
  ;  [item-lister :my-item-lister {:element    #'my-element
  ;                                :subscriber [::get-common-props]
  ;                                :value-path [:my :items]}]
  ;
  ; @return (component)
  ([lister-props] [item-lister nil lister-props])

  ([lister-id lister-props]
   (let [lister-id (a/id lister-id)]
        [components/stated lister-id {:component          #'items/view
                                      :initial-props      lister-props
                                      :initial-props-path (engine/lister-props-path lister-id)}])))
