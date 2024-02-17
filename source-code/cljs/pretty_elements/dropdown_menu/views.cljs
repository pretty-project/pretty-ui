
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
  [menu-id menu-props]
  [:div (dropdown-menu.attributes/menu-attributes menu-id menu-props)
        [:div (dropdown-menu.attributes/menu-body-attributes menu-id menu-props)
              (let [bar-id    (pretty-elements.engine/element-id->subitem-id menu-id :menu-bar)
                    bar-props (dropdown-menu.prototypes/bar-props-prototype  menu-id menu-props)]
                   [menu-bar.views/view bar-id bar-props])
              (let [expandable-id    (pretty-elements.engine/element-id->subitem-id       menu-id :expandable)
                    expandable-props (dropdown-menu.prototypes/expandable-props-prototype menu-id menu-props)]
                   [expandable.views/view expandable-id expandable-props])]])

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
  ; [Menu-bar](pretty-ui/cljs/pretty-elements/api.html#menu-bar)
  ; [Expandable](pretty-ui/cljs/pretty-elements/api.html#expandable)
  ;
  ; @links Implemented properties
  ; [Class properties](pretty-core/cljs/pretty-properties/api.html#class-properties)
  ; [Lifecycle properties](pretty-core/cljs/pretty-properties/api.html#lifecycle-properties)
  ; [Preset properties](pretty-core/cljs/pretty-properties/api.html#preset-properties)
  ; [Progress properties](pretty-core/cljs/pretty-properties/api.html#progress-properties)
  ; [Space properties](pretty-core/cljs/pretty-properties/api.html#space-properties)
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
  ;                              :menu-item-default {:border-radius {:all :s}
  ;                                                  :font-size     :s
  ;                                                  :hover-color   :highlight
  ;                                                  :indent        {:all :xxs}}
  ;                              :menu-items        [{:label "My menu item #1" :dropdown-content [:div "My dropdown content #1"]}
  ;                                                  {:label "My menu item #2" :dropdown-content [:div "My dropdown content #2"]}
  ;                                                  {:label "My menu item #3" :dropdown-content [:div "My dropdown content #3"]}}]
  ([menu-props]
   [view (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   ; @note (tutorials#parameterizing)
   (fn [_ menu-props]
       (let [menu-props (pretty-presets.engine/apply-preset                     menu-id menu-props)
             menu-props (dropdown-menu.prototypes/menu-props-prototype          menu-id menu-props)
             menu-props (pretty-elements.engine/element-subitem<-disabled-state menu-id menu-props :menu-bar)
             menu-props (pretty-elements.engine/element-subitem<-disabled-state menu-id menu-props :expandable)
             menu-props (pretty-elements.engine/leave-element-disabled-state    menu-id menu-props :menu-bar)
             menu-props (pretty-elements.engine/leave-element-disabled-state    menu-id menu-props :expandable)]
            [view-lifecycles menu-id menu-props]))))
