
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.api
    (:require [x.app-tools.clipboard.effects]
              [x.app-tools.clipboard.engine]
              [x.app-tools.clipboard.views]
              [x.app-tools.file-saver.effects]
              [x.app-tools.file-saver.engine]
              [x.app-tools.file-saver.side-effects]
              [x.app-tools.file-saver.views]
              [x.app-tools.infinite-loader.effects]
              [x.app-tools.infinite-loader.engine]
              [x.app-tools.infinite-loader.subs]
              [x.app-tools.scheduler.effects]
              [x.app-tools.scheduler.engine]
              [x.app-tools.scheduler.events]
              [x.app-tools.scheduler.subs]
              [x.app-tools.clipboard.side-effects     :as clipboard.side-effects]
              [x.app-tools.image-loader               :as image-loader]
              [x.app-tools.image-preloader            :as image-preloader]
              [x.app-tools.infinite-loader.events     :as infinite-loader.events]
              [x.app-tools.infinite-loader.views      :as infinite-loader.views]
              [x.app-tools.temporary-component.engine :as temporary-component.engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-tools.clipboard.side-effects
(def copy-to-clipboard! clipboard.side-effects/copy-to-clipboard!)

; x.app-tools.image-loader
(def image-loader image-loader/component)

; x.app-tools.image-preloader
(def image-preloader image-preloader/component)

; x.app-tools.infinite-loader.events
(def pause-infinite-loader!  infinite-loader.events/pause-infinite-loader!)
(def restart-infinite-loader infinite-loader.events/restart-infinite-loader!)

; x.app-tools.infinite-loader.views
(def infinite-loader infinite-loader.views/component)

; x.app-tools.temporary-component.engine
(def append-temporary-component! temporary-component.engine/append-temporary-component!)
(def remove-temporary-component! temporary-component.engine/remove-temporary-component!)
