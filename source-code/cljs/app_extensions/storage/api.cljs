
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-extensions.storage.api
    (:require [app-extensions.storage.capacity-handler.subs]
              [app-extensions.storage.directory-creator.effects]
              [app-extensions.storage.directory-creator.events]
              [app-extensions.storage.file-uploader.effects]
              [app-extensions.storage.file-uploader.events]
              [app-extensions.storage.file-uploader.side-effects]
              [app-extensions.storage.file-uploader.subs]
              [app-extensions.storage.media-browser.effects]
              [app-extensions.storage.media-picker.effects]
              [app-extensions.storage.media-picker.events]
              [app-extensions.storage.media-picker.subs]
              [app-extensions.storage.media-viewer.effects]
              [app-extensions.storage.media-viewer.events]
              [app-extensions.storage.media-viewer.subs]
              [app-extensions.storage.media-picker.views :as media-picker.views]
              [app-extensions.storage.media-viewer.views :as media-viewer.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; app-extensions.storage.media-picker.views
(def media-picker media-picker.views/element)

; app-extensions.storage.media-viewer.views
(def media-viewer media-viewer.views/element)
