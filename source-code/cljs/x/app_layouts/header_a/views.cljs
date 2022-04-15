
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.header-a.views
    (:require [mid-fruits.css                 :as css]
              [reagent.api                    :as reagent]
              [x.app-components.api           :as components]
              [x.app-core.api                 :as a]
              [x.app-layouts.header-a.helpers :as header-a.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ;  {:description (metamorphic-content)(opt)
  ;   :label (metamorphic-content)
  ;   :offset (px)(opt)}
  [_ {:keys [description label offset]}]
  [:div#x-header-a [:div#x-header-a--sensor (if offset {:style {:top (css/px offset)}})]
                   [:div#x-header-a--label  (components/content label)]
                   (if description [:div#x-header-a--description (components/content description)])])

(defn- header-a
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  (reagent/lifecycles {:component-did-mount    (fn [] (header-a.helpers/header-did-mount-f    header-id header-props))
                       :component-will-unmount (fn [] (header-a.helpers/header-will-unmount-f header-id))
                       :component-did-update   (fn [this] (let [[_ header-props] (reagent/arguments this)]
                                                               (header-a.helpers/header-did-update-f header-id header-props)))
                       :reagent-render         (fn [_ header-props] [header-structure header-id header-props])}))

(defn header
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  {:description (metamorphic-content)(opt)
  ;   :label (metamorphic-content)
  ;   :offset (px)(opt)}
  ;
  ; @usage
  ;  [layouts/header-a {...}]
  ;
  ; @usage
  ;  [layouts/header-a :my-header {...}]
  ([header-props]
   [header (a/id) header-props])

  ([header-id header-props]
   [header-a header-id header-props]))
