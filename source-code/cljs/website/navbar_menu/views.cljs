
(ns website.navbar-menu.views
    (:require [elements.api                   :as elements]
              [random.api                     :as random]
              [website.navbar-menu.prototypes :as navbar-menu.prototypes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- navbar-menu
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  [elements/dropdown-menu menu-id menu-props])

(defn component
  ; XXX#0715 (source-code/cljs/elements/dropdown_menu/views.cljs)
  ; The navbar-menu component is based on the dropdown-menu element.
  ; For more information check out the documentation of the dropdown-menu component.
  ;
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {}
  ;
  ; @usage
  ; [navbar-menu {...}]
  ;
  ; @usage
  ; [navbar-menu :my-navbar-menu {...}]
  ([menu-props]
   [component (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   (let [] ; menu-props (navbar-menu.prototypes/menu-props-prototype menu-props)
        [navbar-menu menu-id menu-props])))
