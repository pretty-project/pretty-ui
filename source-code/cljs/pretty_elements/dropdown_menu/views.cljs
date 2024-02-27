
(ns pretty-elements.dropdown-menu.views
    (:require [fruits.random.api                        :as random]
              [pretty-elements.dropdown-menu.attributes :as dropdown-menu.attributes]
              [pretty-elements.dropdown-menu.prototypes :as dropdown-menu.prototypes]
              [pretty-elements.engine.api               :as pretty-elements.engine]
              [pretty-elements.menu-bar.views           :as menu-bar.views]
              [pretty-elements.expandable.views         :as expandable.views]
              [pretty-presets.engine.api                :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- dropdown-menu
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {:expandable (map)(opt)
  ;  :menu-bar (map)(opt)
  ;  ...}
  [menu-id {:keys [expandable menu-bar] :as menu-props}]
  [:div (dropdown-menu.attributes/menu-attributes menu-id menu-props)
        [:div (dropdown-menu.attributes/menu-inner-attributes menu-id menu-props)
              (if menu-bar   [menu-bar.views/view   menu-id menu-bar])
              (if expandable [expandable.views/view menu-id expandable])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    menu-id menu-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount menu-id menu-props))
                         :reagent-render         (fn [_ menu-props] [dropdown-menu menu-id menu-props])}))

(defn view
  ; @description
  ; Dropdown style menu.
  ;
  ; @links Implemented elements
  ; [Expandable](pretty-ui/cljs/pretty-elements/api.html#expandable)
  ; [Menu-bar](pretty-ui/cljs/pretty-elements/api.html#menu-bar)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
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
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; Check out the implemented elements.
  ; Check out the implemented properties.
  ;
  ; @usage (pretty-elements/dropdown-menu.png)
  ; [dropdown-menu {:expandable {:border-color  :muted
  ;                              :border-radius {:all :m}
  ;                              :fill-color    :highlight
  ;                              :indent        {:all :s}
  ;                              :outdent       {:top :m}}
  ;                 :menu-bar   {:gap               :xs
  ;                              :menu-item-default {:border-radius {:all :s} :hover-color :highlight :label {:font-size :s} :indent {:all :xxs}}
  ;                              :menu-items        [{:label {:content "My menu item #1"} :dropdown-content [:div "My dropdown content #1"]}
  ;                                                  {:label {:content "My menu item #2"} :dropdown-content [:div "My dropdown content #2"]}
  ;                                                  {:label {:content "My menu item #3"} :dropdown-content [:div "My dropdown content #3"]}}]
  ([menu-props]
   [view (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   ; @note (tutorials#parameterizing)
   (fn [_ menu-props]
       (let [menu-props (pretty-presets.engine/apply-preset            menu-id menu-props)
             menu-props (dropdown-menu.prototypes/menu-props-prototype menu-id menu-props)]
            [view-lifecycles menu-id menu-props]))))
