
(ns components.api
    (:require [components.consent-dialog.effects]
              [components.context-menu.effects]
              [components.compact-list-header.views      :as compact-list-header.views]
              [components.content-swapper-button.views   :as content-swapper-button.views]
              [components.content-swapper-header.views   :as content-swapper-header.views]
              [components.copyright-label.views          :as copyright-label.views]
              [components.data-element.views             :as data-element.views]
              [components.error-content.views            :as error-content.views]
              [components.error-label.views              :as error-label.views]
              [components.ghost-view.views               :as ghost-view.views]
              [components.illustration.views             :as illustration.views]
              [components.input-block.views              :as input-block.views]
              [components.input-table.views              :as input-table.views]
              [components.item-list-header.views         :as item-list-header.views]
              [components.item-list-row.views            :as item-list-row.views]
              [components.list-item-avatar.views         :as list-item-avatar.views]
              [components.list-item-button.views         :as list-item-button.views]
              [components.list-item-cell.views           :as list-item-cell.views]
              [components.list-item-drag-handle.views    :as list-item-drag-handle.views]
              [components.list-item-gap.views            :as list-item-gap.views]
              [components.list-item-icon.views           :as list-item-icon.views]
              [components.list-item-thumbnail.views      :as list-item-thumbnail.views]
              [components.menu-table.views               :as menu-table.views]
              [components.notification-bubble.views      :as notification-bubble.views]
              [components.pdf-preview.views              :as pdf-preview.views]
              [components.popup-label-bar.views          :as popup-label-bar.views]
              [components.popup-menu-header.views        :as popup-menu-header.views]
              [components.popup-menu-title.views         :as popup-menu-title.views]
              [components.popup-progress-indicator.views :as popup-progress-indicator.views]
              [components.section-description.views      :as section-description.views]
              [components.section-title.views            :as section-title.views]
              [components.side-menu-footer.views         :as side-menu-footer.views]
              [components.side-menu-header.views         :as side-menu-header.views]
              [components.side-menu-label.views          :as side-menu-label.views]
              [components.side-menu.views                :as side-menu.views]
              [components.user-avatar.views              :as user-avatar.views]
              [components.vector-item-controls.views     :as vector-item-controls.views]
              [components.vector-item-list.views         :as vector-item-list.views]
              [components.vector-items-header.views      :as vector-items-header.views]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (*/view)
(def compact-list-header      compact-list-header.views/view)
(def content-swapper-button   content-swapper-button.views/view)
(def content-swapper-header   content-swapper-header.views/view)
(def copyright-label          copyright-label.views/view)
(def data-element             data-element.views/view)
(def error-content            error-content.views/view)
(def error-label              error-label.views/view)
(def ghost-view               ghost-view.views/view)
(def illustration             illustration.views/view)
(def input-block              input-block.views/view)
(def input-table              input-table.views/view)
(def item-list-header         item-list-header.views/view)
(def item-list-row            item-list-row.views/view)
(def list-item-avatar         list-item-avatar.views/view)
(def list-item-button         list-item-button.views/view)
(def list-item-cell           list-item-cell.views/view)
(def list-item-drag-handle    list-item-drag-handle.views/view)
(def list-item-gap            list-item-gap.views/view)
(def list-item-icon           list-item-icon.views/view)
(def list-item-thumbnail      list-item-thumbnail.views/view)
(def menu-table               menu-table.views/view)
(def notification-bubble      notification-bubble.views/view)
(def pdf-preview              pdf-preview.views/view)
(def popup-label-bar          popup-label-bar.views/view)
(def popup-menu-header        popup-menu-header.views/view)
(def popup-menu-title         popup-menu-title.views/view)
(def popup-progress-indicator popup-progress-indicator.views/view)
(def side-menu                side-menu.views/view)
(def side-menu-footer         side-menu-footer.views/view)
(def side-menu-header         side-menu-header.views/view)
(def side-menu-label          side-menu-label.views/view)
(def section-description      section-description.views/view)
(def section-title            section-title.views/view)
(def user-avatar              user-avatar.views/view)
(def vector-item-controls     vector-item-controls.views/view)
(def vector-item-list         vector-item-list.views/view)
(def vector-items-header      vector-items-header.views/view)
