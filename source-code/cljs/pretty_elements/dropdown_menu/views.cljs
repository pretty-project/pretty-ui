
(ns pretty-elements.dropdown-menu.views
    (:require [fruits.random.api                        :as random]
              [metamorphic-content.api                  :as metamorphic-content]
              [pretty-elements.dropdown-menu.attributes :as dropdown-menu.attributes]
              [pretty-elements.dropdown-menu.env        :as dropdown-menu.env]
              [pretty-elements.dropdown-menu.prototypes :as dropdown-menu.prototypes]
              [pretty-elements.dropdown-menu.state      :as dropdown-menu.state]
              [pretty-elements.menu-bar.views           :as menu-bar.views]
              [pretty-elements.engine.api                        :as pretty-elements.engine]
              [reagent.api                              :as reagent]))

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
              (let [bar-props (dropdown-menu.prototypes/bar-props-prototype menu-props)]
                   [menu-bar.views/element menu-id bar-props])
              (if-let [surface-content (dropdown-menu.env/get-surface-content menu-id menu-props)]
                      [:div (dropdown-menu.attributes/menu-surface-attributes menu-id menu-props)
                            [:div (dropdown-menu.attributes/menu-surface-body-attributes menu-id menu-props)
                                  [metamorphic-content/compose surface-content]]])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    menu-id menu-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount menu-id menu-props))
                       :reagent-render         (fn [_ menu-props] [dropdown-menu menu-id menu-props])}))

(defn element
  ; @note
  ; For more information, check out the documentation of the ['menu-bar'](/pretty-ui/cljs/pretty-elements/api.html#menu-bar) element.
  ;
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:menu-items (maps in vector)
  ;   [{:content (metamorphic-content)(opt)
  ;     :placeholder (metamorphic-content)(opt)
  ;     :preset (keyword)(opt)}]
  ;  :surface (map)(opt)
  ;   {:border-color (keyword or string)(opt)
  ;    :border-position (keyword)(opt)
  ;    :border-radius (map)(opt)
  ;     {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;    :border-width (keyword, px or string)(opt)
  ;    :fill-color (keyword or string)
  ;    :indent (map)(opt)
  ;    :outdent (map)(opt)
  ;    :preset (keyword)(opt)}}
  ;
  ; @usage
  ; [dropdown-menu {...}]
  ;
  ; @usage
  ; [dropdown-menu :my-dropdown-menu {...}]
  ([menu-props]
   [element (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   ; @note (tutorials#parametering)
   (fn [_ menu-props]
       (let [menu-props (dropdown-menu.prototypes/menu-props-prototype menu-id menu-props)]
            [element-lifecycles menu-id menu-props]))))
