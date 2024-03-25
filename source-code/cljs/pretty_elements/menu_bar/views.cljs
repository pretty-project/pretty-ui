
(ns pretty-elements.menu-bar.views
    (:require [fruits.hiccup.api                   :as hiccup]
              [fruits.random.api                   :as random]
              [pretty-elements.engine.api          :as pretty-elements.engine]
              [pretty-elements.menu-bar.attributes :as menu-bar.attributes]
              [pretty-elements.menu-bar.prototypes :as menu-bar.prototypes]
              [pretty-elements.menu-item.views     :as menu-item.views]
              [pretty-elements.methods.api         :as pretty-elements.methods]
              [pretty-subitems.api                 :as pretty-subitems]
              [reagent.core                        :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:menu-items [menu-item.views/SHORTHAND-MAP]})

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
              (letfn [(f0 [dex menu-item] [menu-item.views/view (pretty-subitems/subitem-id id dex) menu-item])]
                     (hiccup/put-with-indexed [:<>] menu-items f0))]])

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
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented models.
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
       (let [props (pretty-elements.methods/apply-element-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-presets        id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (menu-bar.prototypes/props-prototype                  id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
