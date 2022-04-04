
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.api
    (:require [extensions.storage.capacity-handler.subs]
              [extensions.storage.directory-creator.effects]
              [extensions.storage.directory-creator.events]
              [extensions.storage.file-uploader.effects]
              [extensions.storage.file-uploader.events]
              [extensions.storage.file-uploader.side-effects]
              [extensions.storage.file-uploader.subs]
              [extensions.storage.media-browser.effects]
              [extensions.storage.media-picker.subs]
              [extensions.storage.media-selector.effects]
              [extensions.storage.media-selector.events]
              [extensions.storage.media-selector.subs]
              [extensions.storage.media-viewer.effects]
              [extensions.storage.media-viewer.events]
              [extensions.storage.media-viewer.subs]
              [extensions.storage.media-picker.views :as media-picker.views]
              [extensions.storage.media-viewer.views :as media-viewer.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; extensions.storage.media-picker.views
(def media-picker media-picker.views/element)

; extensions.storage.media-viewer.views
(def media-viewer media-viewer.views/element)
