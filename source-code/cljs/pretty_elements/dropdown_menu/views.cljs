
(ns pretty-elements.dropdown-menu.views
    (:require [fruits.random.api                        :as random]
              [metamorphic-content.api                  :as metamorphic-content]
              [pretty-elements.dropdown-menu.attributes :as dropdown-menu.attributes]
              [pretty-elements.dropdown-menu.env        :as dropdown-menu.env]
              [pretty-elements.dropdown-menu.prototypes :as dropdown-menu.prototypes]
              [pretty-elements.dropdown-menu.state      :as dropdown-menu.state]
              [pretty-elements.menu-bar.views           :as menu-bar.views]))

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

(defn element
  ; @info
  ; XXX#0715
  ; Some other items based on the 'dropdown-menu' component and their documentations link here.
  ;
  ; @info
  ; XXX#0713 (source-code/cljs/pretty_elements/menu_bar/views.cljs)
  ; The 'dropdown-menu' element is based on the 'menu-bar' element.
  ; For more information, check out the documentation of the 'menu-bar' element.
  ;
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:menu-items (maps in vector)
  ;   [{:content (metamorphic-content)
  ;     :preset (keyword)(opt)}]
  ;  :surface (map)(opt)
  ;   {:border-color (keyword or string)(opt)
  ;    :border-position (keyword)(opt)
  ;    :border-radius (map)(opt)
  ;    :border-width (keyword)(opt)
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
   (fn [_ menu-props] ; XXX#0106 (README.md#parametering)
       (let [menu-props (dropdown-menu.prototypes/menu-props-prototype menu-id menu-props)]
            [dropdown-menu menu-id menu-props]))))
