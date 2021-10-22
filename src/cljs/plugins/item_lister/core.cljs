
(ns plugins.item-lister.core
    (:require  [app-fruits.reagent         :refer [ratom lifecycles]]
               [plugins.item-lister.engine :as engine]
               [plugins.item-lister.header :as header]
               [plugins.item-lister.items  :as items]
               [x.app-components.api       :as components]
               [x.app-layouts.api          :as layouts]
               [x.app-core.api             :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn stated-item-lister
  [lister-id {:keys [icon label label-suffix] :as lister-props}]
  [layouts/layout-a lister-id{:icon icon :label label :label-suffix label-suffix
                              :body        {:content       #'items/view
                                            :content-props lister-props}
                              :body-header {:content       #'header/search-bar
                                            :content-props lister-props}}])

(defn item-lister
  ; @param (keyword)(opt) lister-id
  ; @param (map) lister-props
  ;  {:common-props (map)(opt)
  ;   :element (component)
  ;   :icon (keyword)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :label-suffix (metamorphic-content)(opt)
  ;   :on-list-ended (metamorphic-event)(opt)
  ;    Az elemeket megjelenítő lista végére érve megtörténő esemény
  ;   :on-order-change (metamorphic-event)(opt)
  ;    Az elemek sorrendjének megváltozásakor megtörténő esemény
  ;    Only w/ {:sortable? true}
  ;   :on-search (metamorphic-event)(opt)
  ;   :request-id (keyword)(opt)
  ;    Az elemek felsorolása alatt megjelenített request-indicator, milyen
  ;    azonosítójú request aktivitását jelezze vissza
  ;   :sortable? (boolean)(opt)
  ;    Default: false
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
  ; @return (component)
  ([lister-props] [item-lister nil lister-props])

  ([lister-id lister-props]
   (let [lister-id (a/id lister-id)]
        [components/stated lister-id {:component          #'stated-item-lister
                                      :initial-props      lister-props
                                      :initial-props-path (engine/lister-props-path lister-id)}])))
