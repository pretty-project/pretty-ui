
(ns website.multi-menu.views
    (:require [elements.api                  :as elements]
              [random.api                    :as random]
              [website.multi-menu.attributes :as multi-menu.attributes]
              [website.multi-menu.prototypes :as multi-menu.prototypes]
              [website.sidebar.views         :as sidebar.views]
              [window-observer.api           :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-menu
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  [menu-id menu-props]
  [:div "..."])

(defn- multi-menu
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {}
  [menu-id {:keys [threshold] :as menu-props}]
  (if (window-observer/viewport-width-min? threshold)
      [elements/dropdown-menu  menu-id menu-props]
      [sidebar.views/component menu-id {:content [sidebar-menu menu-id menu-props]}]))

(defn component
  ; XXX#0715 (source-code/cljs/elements/dropdown_menu/views.cljs)
  ; The multi-menu component is based on the dropdown-menu element.
  ; For more information check out the documentation of the dropdown-menu element.
  ;
  ; @description
  ; This component implements the dropdown-menu element and in case of the viewport
  ; width is smaller than the given threshold, it displays the menu items
  ; on a sidebar menu and replaces the menu bar with a single menu button.
  ;
  ; @param (keyword)(opt) menu-id
  ; @param (map) menu-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :style (map)(opt)
  ;  :threshold (px)(opt)
  ;   Default: 0}
  ;
  ; @usage
  ; [multi-menu {...}]
  ;
  ; @usage
  ; [multi-menu :my-multi-menu {...}]
  ([menu-props]
   [component (random/generate-keyword) menu-props])

  ([menu-id menu-props]
   (let [menu-props (multi-menu.prototypes/menu-props-prototype menu-props)]
        [multi-menu menu-id menu-props])))
