
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-b.views
    (:require [x.app-core.api                    :as a]
              [x.app-elements.api                :as elements]
              [x.app-layouts.layout-b.helpers    :as layout-b.helpers]
              [x.app-layouts.layout-b.prototypes :as layout-b.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- card-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; @param (map) card-props
  ;  {:icon (keyword)(opt)
  ;   :label (metamorphic-content)}
  [_ _ {:keys [icon label]}]
  ; Ha a label értéke hosszú, és a kártya jobb oldali széléig ér, akkor az {:indent {:right :xs}}
  ; beállítás használatával ...
  [elements/label {:content label
                   :icon    icon
                   :indent  {:right :xs}}])

(defn- card
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; @param (map) card-props
  ;  {:badge-color (keyword)(opt)
  ;   :min-width (keyword)(opt)
  ;   :on-click (metamorphic-event)(opt)}
  [layout-id layout-props {:keys [badge-color min-width on-click] :as card-props}]
  [elements/card {:badge-color      badge-color
                  :content          [card-label layout-id layout-props card-props]
                  :horizontal-align :left
                  :min-width        (or min-width :m)
                  :on-click         on-click}])

(defn- card-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {:cards (maps in vector)}
  [layout-id {:keys [cards] :as layout-props}]
  (letfn [(f [card-list card-props] (conj card-list [card layout-id layout-props card-props]))]
         (reduce f [:div#x-layout-b--card-group] cards)))

(defn- layout-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  [layout-id layout-props]
  [:div#x-layout-b (layout-b.helpers/layout-body-attributes layout-id layout-props)
                   [card-list layout-id layout-props]])

(defn- layout-b
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  [layout-id layout-props]
  [layout-body layout-id layout-props])

(defn layout
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {:cards (maps in vector)
  ;    [{:badge-color (keyword)(opt)
  ;       :primary, :secondary, :warning, :success
  ;      :icon (keyword)(opt)
  ;      :icon-family (keyword)(opt)
  ;       Default: :material-icons-filled
  ;       Only w/ {:icon ...}
  ;      :label (metamorphic-content)
  ;      :min-width (keyword)(opt)
  ;       :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;       Default: :m
  ;      :on-click (metamorphic-event)(opt)}
  ;     {...}]
  ;   :class (keyword or keywords in vector)(opt)
  ;   :horizontal-align (keyword)(opt)
  ;    :left, :center, :right
  ;    Default: :center
  ;   :style (map)(opt)}
  ;
  ; @usage
  ;  [layouts/layout-b {...}]
  ;
  ; @usage
  ;  [layouts/layout-b :my-layout {...}]
  ([layout-props]
   [layout (a/id) layout-props])

  ([layout-id layout-props]
   (let [layout-props (layout-b.prototypes/layout-props-prototype layout-props)]
        [layout-b layout-id layout-props])))
