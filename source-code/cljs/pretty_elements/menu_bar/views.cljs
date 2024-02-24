
(ns pretty-elements.menu-bar.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [pretty-elements.engine.api          :as pretty-elements.engine]
              [pretty-elements.menu-bar.attributes :as menu-bar.attributes]
              [pretty-elements.menu-bar.prototypes :as menu-bar.prototypes]
              [pretty-elements.menu-item.views     :as menu-item.views]
              [pretty-presets.engine.api           :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar-menu-item
  ; @ignore
  ;
  ; @param (integer) item-dex
  ; @param (map) item-props
  [item-dex item-props]
  (let [item-props (menu-bar.prototypes/item-props-prototype item-dex item-props)]
       [menu-item.views/view item-props]))

(defn- menu-bar
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  ; {:menu-items (maps in vector)(opt)
  ;  ...}
  [bar-id {:keys [menu-items] :as bar-props}]
  [:div (menu-bar.attributes/menu-bar-attributes bar-id bar-props)
        [:div (menu-bar.attributes/menu-bar-inner-attributes bar-id bar-props)
              (letfn [(f0 [item-dex item-props] [menu-bar-menu-item item-dex item-props])]
                     (hiccup/put-with-indexed [:<>] menu-items f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) bar-id
  ; @param (map) bar-props
  [bar-id bar-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    bar-id bar-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount bar-id bar-props))
                         :reagent-render         (fn [_ bar-props] [menu-bar bar-id bar-props])}))

(defn view
  ; @description
  ; Menu bar element.
  ;
  ; @links Implemented elements
  ; [Menu-item](pretty-ui/cljs/pretty-elements/api.html#menu-item)
  ;
  ; @links Implemented properties
  ; [Background color properties](pretty-core/cljs/pretty-properties/api.html#background-color-properties)
  ; [Border properties](pretty-core/cljs/pretty-properties/api.html#border-properties)
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Flex properties](pretty-core/cljs/pretty-properties/api.html#flex-properties)
  ; [Inner position properties](pretty-core/cljs/pretty-properties/api.html#inner-position-properties)
  ; [Inner size properties](pretty-core/cljs/pretty-properties/api.html#inner-size-properties)
  ; [Inner space properties](pretty-core/cljs/pretty-properties/api.html#inner-space-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ;
  ; @param (keyword)(opt) bar-id
  ; @param (map) bar-props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/menu-bar.png)
  ; [menu-bar {:gap               :m
  ;            :menu-item-default {:border-position :bottom
  ;                                :border-width    :xs
  ;                                :font-size       :s}
  ;            :menu-items        [{:label "My menu item #1" :href-uri "/my-uri-1" :border-color :secondary}
  ;                                {:label "My menu item #2" :href-uri "/my-uri-2"}
  ;                                {:label "My menu item #3" :href-uri "/my-uri-3"}]}]
  ([bar-props]
   [view (random/generate-keyword) bar-props])

  ([bar-id bar-props]
   ; @note (tutorials#parameterizing)
   (fn [_ bar-props]
       (let [bar-props (pretty-presets.engine/apply-preset      bar-id bar-props)
             bar-props (menu-bar.prototypes/bar-props-prototype bar-id bar-props)]
            [view-lifecycles bar-id bar-props]))))
