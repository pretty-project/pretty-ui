
(ns pretty-elements.dropdown-menu.views
    (:require [fruits.random.api                        :as random]
              [pretty-elements.dropdown-menu.attributes :as dropdown-menu.attributes]
              [pretty-elements.dropdown-menu.prototypes :as dropdown-menu.prototypes]
              [pretty-elements.engine.api               :as pretty-elements.engine]
              [pretty-elements.expandable.views         :as expandable.views]
              [pretty-elements.menu-bar.views           :as menu-bar.views]
              [pretty-elements.methods.api              :as pretty-elements.methods]
              [pretty-subitems.api                      :as pretty-subitems]
              [reagent.core                             :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(def SHORTHAND-MAP {:expandable expandable.views/SHORTHAND-MAP
                    :menu-bar   menu-bar.views/SHORTHAND-MAP})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- dropdown-menu
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:expandable (map)(opt)
  ;  :menu-bar (map)(opt)
  ;  ...}
  [id {:keys [expandable menu-bar] :as props}]
  [:div (dropdown-menu.attributes/outer-attributes id props)
        [:div (dropdown-menu.attributes/inner-attributes id props)
              (if menu-bar   [menu-bar.views/view   (pretty-subitems/subitem-id id :menu-bar)   menu-bar])
              (if expandable [expandable.views/view (pretty-subitems/subitem-id id :expandable) expandable])]])

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
                         :reagent-render         (fn [_ props] [dropdown-menu id props])}))

(defn view
  ; @description
  ; Dropdown style menu.
  ;
  ; @links Implemented elements
  ; [Expandable](pretty-ui/cljs/pretty-elements/api.html#expandable)
  ; [Menu-bar](pretty-ui/cljs/pretty-elements/api.html#menu-bar)
  ;
  ; @links Implemented models
  ; [Flex container model](pretty-core/cljs/pretty-models/api.html#flex-container-model)
  ;
  ; @param (keyword)(opt) id
  ; @param (map) props
  ; Check out the implemented elements.
  ; Check out the implemented models.
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
  ([props]
   [view (random/generate-keyword) props])

  ([id props]
   ; @note (tutorials#parameterizing)
   (fn [_ props]
       (let [props (pretty-elements.methods/apply-element-shorthand-map  id props SHORTHAND-MAP)
             props (pretty-elements.methods/apply-element-preset         id props)
             props (pretty-elements.methods/import-element-dynamic-props id props)
             props (pretty-elements.methods/import-element-state-events  id props)
             props (pretty-elements.methods/import-element-state         id props)
             props (dropdown-menu.prototypes/props-prototype             id props)]
            (if (:mounted? props)
                [view-lifecycles id props])))))
