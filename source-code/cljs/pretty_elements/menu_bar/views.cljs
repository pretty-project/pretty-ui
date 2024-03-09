
(ns pretty-elements.menu-bar.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [pretty-elements.engine.api          :as pretty-elements.engine]
              [pretty-elements.menu-bar.attributes :as menu-bar.attributes]
              [pretty-elements.menu-bar.prototypes :as menu-bar.prototypes]
              [pretty-elements.menu-item.views     :as menu-item.views]
              [pretty-presets.engine.api           :as pretty-presets.engine]
              [reagent.core                        :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:menu-items (maps in vector)(opt)
  ;  ...}
  [id {:keys [menu-items] :as props}]
  [:div (menu-bar.attributes/outer-attributes id props)
        [:div (menu-bar.attributes/inner-attributes id props)
              (letfn [(f0 [menu-item] [menu-item.views/view menu-item])]
                     (hiccup/put-with [:<>] menu-items f0))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  [id props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    id props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount id props))
                         :reagent-render         (fn [_ props] [menu-bar id props])}))

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
  ; [Mouse event properties](pretty-core/cljs/pretty-properties/api.html#mouse-event-properties)
  ; [Outer position properties](pretty-core/cljs/pretty-properties/api.html#outer-position-properties)
  ; [Outer size properties](pretty-core/cljs/pretty-properties/api.html#outer-size-properties)
  ; [Outer space properties](pretty-core/cljs/pretty-properties/api.html#outer-space-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [State properties](pretty-core/cljs/pretty-properties/api.html#state-properties)
  ; [Style properties](pretty-core/cljs/pretty-properties/api.html#style-properties)
  ; [Theme properties](pretty-core/cljs/pretty-properties/api.html#theme-properties)
  ; [Visibility properties](pretty-core/cljs/pretty-properties/api.html#visibility-properties)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/menu-bar.png)
  ; [menu-bar {:gap               :m
  ;            :menu-item-default {:border-position :bottom :border-width :xs :label {:font-size :s}}
  ;            :menu-items        [{:label {:content "My menu item #1"} :href-uri "/my-uri-1" :border-color :secondary}
  ;                                {:label {:content "My menu item #2"} :href-uri "/my-uri-2"}
  ;                                {:label {:content "My menu item #3"} :href-uri "/my-uri-3"}]}]
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-presets.engine/apply-preset  id props)
             props (menu-bar.prototypes/props-prototype id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
