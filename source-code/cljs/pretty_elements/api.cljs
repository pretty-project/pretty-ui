
(ns pretty-elements.api
    (:require [pretty-elements.adornment-group.views      :as adornment-group.views]
              [pretty-elements.adornment.views            :as adornment.views]
              [pretty-elements.anchor.views               :as anchor.views]
              [pretty-elements.blank.views                :as blank.views]
              [pretty-elements.button.views               :as button.views]
              [pretty-elements.card.views                 :as card.views]
              [pretty-elements.chip-group.views           :as chip-group.views]
              [pretty-elements.chip.views                 :as chip.views]
              [pretty-elements.column.views               :as column.views]
              [pretty-elements.crumb-group.views          :as crumb-group.views]
              [pretty-elements.crumb.views                :as crumb.views]
              [pretty-elements.dropdown-menu.views        :as dropdown-menu.views]
              [pretty-elements.expandable.side-effects    :as expandable.side-effects]
              [pretty-elements.expandable.views           :as expandable.views]
              [pretty-elements.ghost.views                :as ghost.views]
              [pretty-elements.header.views               :as header.views]
              [pretty-elements.horizontal-separator.views :as horizontal-separator.views]
              [pretty-elements.horizontal-spacer.views    :as horizontal-spacer.views]
              [pretty-elements.icon-button.views          :as icon-button.views]
              [pretty-elements.image.views                :as image.views]
              [pretty-elements.menu-bar.views             :as menu-bar.views]
              [pretty-elements.menu-item.views            :as menu-item.views]
              [pretty-elements.notification-bubble.views  :as notification-bubble.views]
              [pretty-elements.row.views                  :as row.views]
              [pretty-elements.stepper.views              :as stepper.views]
              [pretty-elements.text.views                 :as text.views]
              [pretty-elements.thumbnail.views            :as thumbnail.views]
              [pretty-elements.toggle.views               :as toggle.views]
              [pretty-elements.vertical-separator.views   :as vertical-separator.views]
              [pretty-elements.vertical-spacer.views      :as vertical-spacer.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial Implemented accessories
;
; Accessories, implemented by elements are customizable by
;
; @example Customizing the cover accessory on a button element:
; [button {:cover {:fill-color :primary ...}
;          :label "My button"}]

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial Implemented elements
;
; Some element implements other elements.
; For example, the 'menu-bar' element displays a set of configured 'menu-item' elements.
;
; @usage
; [menu-bar {:menu-items [{:label "Menu item #1" :href "/my-uri"}]
;            ...}]

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial Implemented properties
;
; Elements implement property models.
;
; @usage
; [button {:font-size :xs :font-weight :medium :letter-spacing :auto ...}]

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (pretty-elements.expandable.side-effects/*)
(def expand-content!   expandable.side-effects/expand-content!)
(def collapse-content! expandable.side-effects/collapse-content!)

; @redirect (*/view)
(def adornment            adornment.views/view)
(def adornment-group      adornment-group.views/view)
(def anchor               anchor.views/view)
(def blank                blank.views/view)
(def button               button.views/view)
(def card                 card.views/view)
(def chip                 chip.views/view)
(def chip-group           chip-group.views/view)
(def column               column.views/view)
(def crumb                crumb.views/view)
(def crumb-group          crumb-group.views/view)
(def dropdown-menu        dropdown-menu.views/view)
(def expandable           expandable.views/view)
(def ghost                ghost.views/view)
(def header               header.views/view)
(def horizontal-separator horizontal-separator.views/view)
(def horizontal-spacer    horizontal-spacer.views/view)
(def icon-button          icon-button.views/view)
(def image                image.views/view)
(def menu-bar             menu-bar.views/view)
(def menu-item            menu-item.views/view)
(def notification-bubble  notification-bubble.views/view)
(def row                  row.views/view)
(def text                 text.views/view)
(def thumbnail            thumbnail.views/view)
(def toggle               toggle.views/view)
(def vertical-separator   vertical-separator.views/view)
(def vertical-spacer      vertical-spacer.views/view)
