
(ns components.api
    (:require [components.consent-dialog.effects]
              [components.context-menu.effects]
              [components.color-picker.views             :as color-picker.views]
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
              [components.side-menu-footer.views         :as side-menu-footer.views]
              [components.side-menu-header.views         :as side-menu-header.views]
              [components.side-menu-label.views          :as side-menu-label.views]
              [components.side-menu.views                :as side-menu.views]
              [components.section-description.views      :as section-description.views]
              [components.section-title.views            :as section-title.views]
              [components.user-avatar.views              :as user-avatar.views]
              [components.vector-item-controls.views     :as vector-item-controls.views]
              [components.vector-item-list.views         :as vector-item-list.views]
              [components.vector-items-header.views      :as vector-items-header.views]
              [window-observer.api                       :as window-observer]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Initializing the viewport resize observer before using subscriptions for viewport dimensions
(window-observer/listen-to-viewport-resize!)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; components.*.views
(def color-picker             color-picker.views/component)
(def compact-list-header      compact-list-header.views/component)
(def content-swapper-button   content-swapper-button.views/component)
(def content-swapper-header   content-swapper-header.views/component)
(def copyright-label          copyright-label.views/component)
(def data-element             data-element.views/component)
(def error-content            error-content.views/component)
(def error-label              error-label.views/component)
(def ghost-view               ghost-view.views/component)
(def illustration             illustration.views/component)
(def input-block              input-block.views/component)
(def input-table              input-table.views/component)
(def item-list-header         item-list-header.views/component)
(def item-list-row            item-list-row.views/component)
(def list-item-avatar         list-item-avatar.views/component)
(def list-item-button         list-item-button.views/component)
(def list-item-cell           list-item-cell.views/component)
(def list-item-drag-handle    list-item-drag-handle.views/component)
(def list-item-gap            list-item-gap.views/component)
(def list-item-icon           list-item-icon.views/component)
(def list-item-thumbnail      list-item-thumbnail.views/component)
(def menu-table               menu-table.views/component)
(def notification-bubble      notification-bubble.views/component)
(def pdf-preview              pdf-preview.views/component)
(def popup-label-bar          popup-label-bar.views/component)
(def popup-menu-header        popup-menu-header.views/component)
(def popup-menu-title         popup-menu-title.views/component)
(def popup-progress-indicator popup-progress-indicator.views/component)
(def side-menu                side-menu.views/component)
(def side-menu-footer         side-menu-footer.views/component)
(def side-menu-header         side-menu-header.views/component)
(def side-menu-label          side-menu-label.views/component)
(def section-description      section-description.views/component)
(def section-title            section-title.views/component)
(def user-avatar              user-avatar.views/component)
(def vector-item-controls     vector-item-controls.views/component)
(def vector-item-list         vector-item-list.views/component)
(def vector-items-header      vector-items-header.views/component)
