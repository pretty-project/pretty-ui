
(ns elements.dropdown-menu.views
    (:require [elements.dropdown-menu.attributes :as dropdown-menu.attributes]
              [elements.dropdown-menu.prototypes :as dropdown-menu.prototypes]
              [elements.dropdown-menu.state      :as dropdown-menu.state]
              [elements.menu-bar.views           :as menu-bar.views]
              [random.api                        :as random]
              [x.components.api                  :as x.components]))

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
              [menu-bar.views/element menu-id menu-props]
              (if-let [content @dropdown-menu.state/VISIBLE-CONTENT]
                      [:div (dropdown-menu.attributes/menu-content-attributes menu-id menu-props)
                            [x.components/content menu-id content]])]])

(defn element
  ; XXX#0715
  ; Some other items based on the dropdown-menu component and their documentations are linked to here.
  ;
  ; XXX#0713 (source-code/cljs/elements/menu_bar/views.cljs)
  ; The dropdown-menu element is based on the menu-bar element.
  ; For more information check out the documentation of the menu-bar element.
  ;
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:menu-items (maps in vector)
  ;   [{:content (metamorphic-content)}]}
  ;
  ; @usage
  ; [dropdown-menu {...}]
  ;
  ; @usage
  ; [dropdown-menu :my-dropdown-menu {...}]
  ([menu-props]
   [element (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   (let [menu-props (dropdown-menu.prototypes/menu-props-prototype menu-props)]
        [dropdown-menu menu-id menu-props])))
