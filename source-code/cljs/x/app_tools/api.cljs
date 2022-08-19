

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.api
    (:require [x.app-tools.clipboard.effects]
              [x.app-tools.file-saver.effects]
              [x.app-tools.file-saver.side-effects]
              [x.app-tools.infinite-loader.effects]
              [x.app-tools.infinite-loader.subs]
              [x.app-tools.scheduler.effects]
              [x.app-tools.scheduler.events]
              [x.app-tools.scheduler.subs]
              [x.app-tools.clipboard.side-effects           :as clipboard.side-effects]
              [x.app-tools.image-loader.views               :as image-loader.views]
              [x.app-tools.image-preloader.views            :as image-preloader.views]
              [x.app-tools.infinite-loader.events           :as infinite-loader.events]
              [x.app-tools.infinite-loader.views            :as infinite-loader.views]
              [x.app-tools.temporary-component.side-effects :as temporary-component.side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-tools.clipboard.side-effects
(def copy-to-clipboard! clipboard.side-effects/copy-to-clipboard!)

; x.app-tools.image-loader.views
(def image-loader image-loader.views/component)

; x.app-tools.image-preloader.views
(def image-preloader image-preloader.views/component)

; x.app-tools.infinite-loader.events
(def pause-infinite-loader!  infinite-loader.events/pause-infinite-loader!)
(def restart-infinite-loader infinite-loader.events/restart-infinite-loader!)

; x.app-tools.infinite-loader.views
(def infinite-loader infinite-loader.views/component)

; x.app-tools.temporary-component.side-effects
(def append-temporary-component! temporary-component.side-effects/append-temporary-component!)
(def remove-temporary-component! temporary-component.side-effects/remove-temporary-component!)
