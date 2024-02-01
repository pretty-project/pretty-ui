
(ns pretty-elements.dropdown-menu.attributes
    (:require [pretty-css.appearance.api           :as pretty-css.appearance]
              [pretty-css.basic.api                :as pretty-css.basic]
              [pretty-css.layout.api               :as pretty-css.layout]
              [pretty-elements.dropdown-menu.state :as dropdown-menu.state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-surface-body-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ {:keys [surface]}]
  (-> {:class :pe-dropdown-menu--surface-body}
      (pretty-css.appearance/background-attributes  surface)
      (pretty-css.appearance/border-attributes surface)
      (pretty-css.layout/indent-attributes surface)))

(defn menu-surface-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ; {}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ {:keys [surface]}]
  (-> {:class :pe-dropdown-menu--surface}
      (pretty-css.layout/outdent-attributes surface)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-body-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ menu-props]
  (-> {:class :pe-dropdown-menu--body}
      (pretty-css.layout/indent-attributes menu-props)
      (pretty-css.basic/style-attributes  menu-props)))

(defn menu-attributes
  ; @ignore
  ;
  ; @param (keyword) menu-id
  ; @param (map) menu-props
  ;
  ; @return (map)
  ; {}
  [menu-id menu-props]
  ; BUG#7016
  ; - Using the on-mouse-out event means that the child elements of the dropdown menu
  ;   such as the items in the menu-bar element could trigger their parent's on-mouse-out
  ;   event by bubbling.
  ; - Moving the cursor over the menu bar could cause flickering by firing the on-mouse-out
  ;   events of the inner child elements.
  ; - Using the stopPropagation function is not a solution because in case the mouse
  ;   leaves the dropdown-menu directly from the menu-bar (no padding around the menu-bar)
  ;   means the dropdown-menu on-mouse-out doesn't fire at all.
  ; - https://www.geeksforgeeks.org/how-to-disable-mouseout-events-triggered-by-child-elements
  ;   According to this article using on-mouse-leave instead of the on-mouse-out event
  ;   solves the problem.
  (-> {:class :pe-dropdown-menu
       :on-mouse-leave #(swap! dropdown-menu.state/MENUS assoc-in [menu-id :active-dex] nil)}
      (pretty-css.basic/class-attributes   menu-props)
      (pretty-css.layout/outdent-attributes menu-props)
      (pretty-css.basic/state-attributes   menu-props)))
